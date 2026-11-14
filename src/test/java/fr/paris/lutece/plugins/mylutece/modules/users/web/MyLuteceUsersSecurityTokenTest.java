/*
 * Copyright (c) 2002-2026, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.mylutece.modules.users.web;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.security.SecurityTokenHandler;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.cdi.mvc.event.MvcEvent.ControllerInvocationType;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import jakarta.inject.Inject;

/**
 * Every action of the users management that changes data is checked by the security token filter: posted without token
 * it is refused, posted with the token issued by the screen of its form (or by its confirmation) it is accepted.
 */
public class MyLuteceUsersSecurityTokenTest extends LuteceTestCase
{
    private static final String RIGHT = "MYLUTECE_USERS_MANAGEMENT";
    private static final String SERVLET_PATH_USERS = "/jsp/admin/plugins/mylutece/modules/users/ManageMyLuteceSearchUsers.jsp";
    private static final String SERVLET_PATH_ROLES = "/jsp/admin/plugins/mylutece/modules/users/ManageMyLuteceUserRoles.jsp";

    @Inject
    private MyLuteceSearchUserJspBean _userJspBean;
    @Inject
    private MyLuteceUserRoleJspBean _roleJspBean;
    @Inject
    private SecurityTokenHandler _securityTokenHandler;

    private static MockHttpServletRequest newRequest( String strServletPath )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( "GET" );
        request.setServletPath( strServletPath );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), RIGHT );
        return request;
    }

    /**
     * Displays the controllers once, as the back office does, so that they are known by the security token handler.
     */
    private void displayControllers( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = newRequest( SERVLET_PATH_USERS );
        request.addParameter( "view", "confirmRemoveMyLuteceSearchUser" );
        _userJspBean.processController( request, new MockHttpServletResponse( ) );
        request = newRequest( SERVLET_PATH_ROLES );
        request.addParameter( "view", "manageMyLuteceUserRoles" );
        _roleJspBean.processController( request, new MockHttpServletResponse( ) );
    }

    private static Method findMethod( Class<?> clazz, Class<? extends java.lang.annotation.Annotation> annotation, String strName )
    {
        return Arrays.stream( clazz.getDeclaredMethods( ) ).filter( m -> m.isAnnotationPresent( annotation ) )
                .filter( m -> strName.equals( ( annotation == View.class ) ? m.getAnnotation( View.class ).value( ) : m.getAnnotation( Action.class ).value( ) ) )
                .findFirst( ).orElseThrow( ( ) -> new AssertionError( "No method for " + strName ) );
    }

    /**
     * The token the core issues when the screen (view or action) is displayed, as the security token filter does.
     */
    private String issueToken( MockHttpServletRequest request, Class<?> clazz, ControllerInvocationType type, String strName )
    {
        if ( type == ControllerInvocationType.ACTION )
        {
            // The token of a page displayed by an action is issued for the action the request names
            request.setParameter( "action", strName );
        }
        _securityTokenHandler.handle( request );
        _securityTokenHandler.handleToken( request, type, findMethod( clazz, type == ControllerInvocationType.ACTION ? Action.class : View.class, strName ) );
        String strToken = _securityTokenHandler.resolveTokenValue( request );
        assertTrue( strToken != null, "No token issued by " + strName );
        return strToken;
    }

    /**
     * Posts the action: refused without token, accepted with the given token.
     */
    private void assertActionNeedsToken( MockHttpServletRequest request, String strAction, String strToken )
    {
        request.setMethod( "POST" );
        request.setParameter( "view", new String [ 0] );
        request.setParameter( SecurityTokenHandler.MARK_CSRF_TOKEN, new String [ 0] );
        request.setParameter( "action", strAction );
        assertFalse( _securityTokenHandler.shouldNotFilter( request ), strAction + " posted without token is not checked by the security token filter" );
        String strTokenKey = _securityTokenHandler.resolveTokenAction( request, strAction );
        assertFalse( _securityTokenHandler.validate( request, strTokenKey ), strAction + " accepted without token" );
        request.setParameter( SecurityTokenHandler.MARK_CSRF_TOKEN, strToken );
        assertTrue( _securityTokenHandler.validate( request, strTokenKey ), strAction + " refused with the token of its screen" );
    }

    @Test
    public void testCreate( ) throws AccessDeniedException
    {
        displayControllers( );
        MockHttpServletRequest request = newRequest( SERVLET_PATH_USERS );
        String strToken = issueToken( request, MyLuteceSearchUserJspBean.class, ControllerInvocationType.VIEW, "createMyLuteceSearchUser" );
        assertActionNeedsToken( request, "createMyLuteceSearchUser", strToken );
    }

    @Test
    public void testModify( ) throws AccessDeniedException
    {
        displayControllers( );
        MockHttpServletRequest request = newRequest( SERVLET_PATH_USERS );
        String strToken = issueToken( request, MyLuteceSearchUserJspBean.class, ControllerInvocationType.VIEW, "modifyMyLuteceSearchUser" );
        assertActionNeedsToken( request, "modifyMyLuteceSearchUser", strToken );
    }

    @Test
    public void testRemoveFromItsConfirmation( ) throws AccessDeniedException
    {
        displayControllers( );
        MockHttpServletRequest request = newRequest( SERVLET_PATH_USERS );
        String strToken = issueToken( request, MyLuteceSearchUserJspBean.class, ControllerInvocationType.VIEW, "confirmRemoveMyLuteceSearchUser" );
        assertActionNeedsToken( request, "removeMyLuteceSearchUser", strToken );
    }

    @Test
    public void testUpdateAttributeMapping( ) throws AccessDeniedException
    {
        displayControllers( );
        MockHttpServletRequest request = newRequest( SERVLET_PATH_USERS );
        String strToken = issueToken( request, MyLuteceSearchUserJspBean.class, ControllerInvocationType.VIEW, "manageAttributeMapping" );
        assertActionNeedsToken( request, "updateAttributeMapping", strToken );
    }

    /**
     * The import screen posts the search; the page of the results, displayed by the search, posts the creation of a user
     * per row.
     */
    @Test
    public void testImportFromProvider( ) throws AccessDeniedException
    {
        displayControllers( );
        MockHttpServletRequest request = newRequest( SERVLET_PATH_USERS );
        String strToken = issueToken( request, MyLuteceSearchUserJspBean.class, ControllerInvocationType.VIEW, "importMyLuteceSearchUser" );
        assertActionNeedsToken( request, "searchUsersFromProvider", strToken );

        request = newRequest( SERVLET_PATH_USERS );
        strToken = issueToken( request, MyLuteceSearchUserJspBean.class, ControllerInvocationType.ACTION, "searchUsersFromProvider" );
        assertActionNeedsToken( request, "createMyLuteceSearchUser", strToken );
    }

    @Test
    public void testAssignRoles( ) throws AccessDeniedException
    {
        displayControllers( );
        MockHttpServletRequest request = newRequest( SERVLET_PATH_ROLES );
        String strToken = issueToken( request, MyLuteceUserRoleJspBean.class, ControllerInvocationType.VIEW, "manageMyLuteceUserRoles" );
        assertActionNeedsToken( request, "assignMyLuteceUserRole", strToken );
    }
}
