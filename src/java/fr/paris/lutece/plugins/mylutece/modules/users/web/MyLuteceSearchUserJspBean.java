/*
 * Copyright (c) 2002-2020, City of Paris
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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.mylutece.business.attribute.AttributeField;
import fr.paris.lutece.plugins.mylutece.business.attribute.AttributeFieldHome;
import fr.paris.lutece.plugins.mylutece.business.attribute.AttributeHome;
import fr.paris.lutece.plugins.mylutece.business.attribute.IAttribute;
import fr.paris.lutece.plugins.mylutece.business.attribute.MyLuteceUserField;
import fr.paris.lutece.plugins.mylutece.business.attribute.MyLuteceUserFieldHome;
import fr.paris.lutece.plugins.mylutece.modules.users.business.AttributeMapping;
import fr.paris.lutece.plugins.mylutece.modules.users.business.AttributeMappingHome;
import fr.paris.lutece.plugins.mylutece.modules.users.business.MyLuteceSearchUserHome;
import fr.paris.lutece.plugins.mylutece.modules.users.service.MyLuteceUserSearchService;
import fr.paris.lutece.plugins.mylutece.service.MyLutecePlugin;
import fr.paris.lutece.plugins.mylutece.service.attribute.MyLuteceUserFieldService;
import fr.paris.lutece.plugins.mylutece.service.search.MyLuteceSearchUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.utils.MVCMessage;
import fr.paris.lutece.util.ErrorMessage;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

/**
 * This class provides the user interface to manage MyLuteceSearchUserfeatures ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageMyLuteceSearchUsers.jsp", controllerPath = "jsp/admin/plugins/mylutece/modules/users/", right = "MYLUTECE_USERS_MANAGEMENT" )
public class MyLuteceSearchUserJspBean extends AbstractmyLuteceUsersManagementJspBean
{
    /**
     *
     */
    private static final long serialVersionUID = 2163559576736974617L;
    // Templates
    private static final String TEMPLATE_MANAGE_SEARCHUSERS = "/admin/plugins/mylutece/modules/users/manage_search_users.html";
    private static final String TEMPLATE_CREATE_SEARCHUSER = "/admin/plugins/mylutece/modules/users/create_search_user.html";
    private static final String TEMPLATE_MODIFY_SEARCHUSER = "/admin/plugins/mylutece/modules/users/modify_search_user.html";
    private static final String TEMPLATE_IMPORT_USERS_FROM_PROVIDER = "/admin/plugins/mylutece/modules/users/import_users_from_provider.html";
    private static final String TEMPLATE_MANAGE_ATTRIBUTE_MAPPING = "/admin/plugins/mylutece/modules/users/manage_attribute_mapping.html";
    // Parameters
    private static final String PARAMETER_ID_SEARCHUSER = "id";
    private static final String PARAMETER_MYLUTECE_ATTRIBUTE_NAME = "attribute";
    private static final String PARAMETER_SEARCH_BY_LAST_NAME = "search_lastName";
    private static final String PARAMETER_SEARCH_BY_GIVEN_NAME = "search_givenName";
    private static final String PARAMETER_SEARCH_BY_EMAIL = "search_email";
    private static final String PARAMETER_FROM_PROVIDER = "import_from_provider";
    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_SEARCHUSERS = "module.mylutece.users.manageUsers.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_SEARCHUSER = "module.mylutece.users.modifyUser.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_SEARCHUSER = "module.mylutece.users.createUser.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MANAGE_ATTRIBUTE_MAPPING = "module.mylutece.users.attributeMapping.pageTitle";
    private static final String PROPERTY_IMPORT_USERS_PAGETITLE = "module.mylutece.users.importUsers.pageTitle";
    // Markers
    private static final String MARK_SEARCHUSER_LIST = "mylutece_search_user_list";
    private static final String MARK_SEARCHUSER = "mylutece_search_user";
    private static final String MARK_MYLUTECE_ATTRIBUTES_LIST = "attribute_list";
    private static final String MARK_PROVIDER_ATTRIBUTES_LIST = "provider_attribute_list";
    private static final String MARK_ATTRIBUTE_MAPPING_LIST = "attribute_mapping_list";
    // Jsp
    private static final String JSP_MANAGE_SEARCHUSERS = "jsp/admin/plugins/mylutece/modules/users/ManageMyLuteceSearchUsers.jsp";
    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_SEARCHUSER = "module.mylutece.users.message.confirmRemoveUser";
    private static final String MESSAGE_FIELD_ERROR_MANDATORY = "module.mylutece.users.validation.field.mandatory";
    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "module.mylutece.users.model.entity.user.";
    // Views
    private static final String VIEW_MANAGE_SEARCHUSERS = "manageMyLuteceSearchUsers";
    private static final String VIEW_CREATE_SEARCHUSER = "createMyLuteceSearchUser";
    private static final String VIEW_MODIFY_SEARCHUSER = "modifyMyLuteceSearchUser";
    private static final String VIEW_IMPORT_SEARCHUSER = "importMyLuteceSearchUser";
    private static final String VIEW_MANAGE_ATTRIBUTE_MAPPING = "manageAttributeMapping";
    // Actions
    private static final String ACTION_CREATE_SEARCHUSER = "createMyLuteceSearchUser";
    private static final String ACTION_MODIFY_SEARCHUSER = "modifyMyLuteceSearchUser";
    private static final String ACTION_REMOVE_SEARCHUSER = "removeMyLuteceSearchUser";
    private static final String ACTION_CONFIRM_REMOVE_SEARCHUSER = "confirmRemoveMyLuteceSearchUser";
    private static final String ACTION_SEARCH_SEARCHUSER = "searchUsersFromProvider";
    private static final String ACTION_UPDATE_ATTRIBUTE_MAPPING = "updateAttributeMapping";
    // Infos
    private static final String INFO_SEARCHUSER_CREATED = "module.mylutece.users.info.user.created";
    private static final String INFO_SEARCHUSER_UPDATED = "module.mylutece.users.info.user.updated";
    private static final String INFO_SEARCHUSER_REMOVED = "module.mylutece.users.info.user.removed";
    private static final String ERROR_SEARCHUSER_PROVIDER_USER_ID_EXIST = "module.mylutece.users.error.providerUserIdExist";
    private static final String INFO_ATTRIBUTE_MAPPING_DONE = "module.mylutece.users.info.attributeMapping.updated";
    // Prefixes
    private static final String PREFIX_ATTRIBUTE_MAPPING = "attribute_mapping_";
    private static final String PREFIX_PROVIDER_ATTRIBUTE = "provider_attribute_";
    // Session variables
    private MyLuteceSearchUser _myLuteceSearchUser;
    private Locale _locale;
    private transient Plugin _myLutecePlugin = PluginService.getPlugin( MyLutecePlugin.PLUGIN_NAME );
    private transient List<IAttribute> _listMyLuteceAttribute;
    private transient List<String> _listProviderAttribute;
    private transient List<AttributeMapping> _listAttributeMapping;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_SEARCHUSERS, defaultView = true )
    public String getManageMyLuteceSearchUsers( HttpServletRequest request )
    {
        _myLuteceSearchUser = null;
        _listMyLuteceAttribute = null;
        List<MyLuteceSearchUser> listMyLuteceSearchUsers = MyLuteceSearchUserHome.getMyLuteceSearchUsersListWithoutAttribute( );
        Map<String, Object> model = getPaginatedListModel( request, MARK_SEARCHUSER_LIST, listMyLuteceSearchUsers, JSP_MANAGE_SEARCHUSERS );
        return getPage( PROPERTY_PAGE_TITLE_MANAGE_SEARCHUSERS, TEMPLATE_MANAGE_SEARCHUSERS, model );
    }

    /**
     * Returns the form to create a myLuteceSearchUser
     *
     * @param request
     *            The Http request
     * @return the html code of the myLuteceSearchUser form
     */
    @View( VIEW_MANAGE_ATTRIBUTE_MAPPING )
    public String getManageAttributeMapping( HttpServletRequest request )
    {
        _listMyLuteceAttribute = gettMyLuteceAttributeWithFields( AttributeHome.findAll( _locale, _myLutecePlugin ) );
        _listProviderAttribute = MyLuteceUserSearchService.getInstance( ).getAllAttributes( );
        _listAttributeMapping = AttributeMappingHome.getAttributeMappingsList( );
        Map<String, Object> model = getModel( );
        model.put( MARK_SEARCHUSER, _myLuteceSearchUser );
        model.put( MARK_ATTRIBUTE_MAPPING_LIST, _listAttributeMapping );
        model.put( MARK_MYLUTECE_ATTRIBUTES_LIST, _listMyLuteceAttribute );
        model.put( MARK_PROVIDER_ATTRIBUTES_LIST, _listProviderAttribute );
        return getPage( PROPERTY_PAGE_TITLE_MANAGE_ATTRIBUTE_MAPPING, TEMPLATE_MANAGE_ATTRIBUTE_MAPPING, model );
    }

    /**
     * Process the data capture form of a new myLuteceSearchUser
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_UPDATE_ATTRIBUTE_MAPPING )
    public String doUpdateAttributeMapping( HttpServletRequest request )
    {
        List<Integer> listMyLuteceAttributeId = new ArrayList<>( );
        Enumeration<String> requestAttributes = request.getParameterNames( );
        while ( requestAttributes.hasMoreElements( ) )
        {
            String parameterName = requestAttributes.nextElement( );
            if ( parameterName.startsWith( PREFIX_ATTRIBUTE_MAPPING ) && !request.getParameter( parameterName ).isEmpty( ) )
            {
                String strMyLuteceAttributeId = parameterName.substring( parameterName.lastIndexOf( "_" ) + 1 );
                AttributeMapping attributeMapping = new AttributeMapping( );
                attributeMapping.setId( Integer.valueOf( strMyLuteceAttributeId ) );
                attributeMapping.setIdProviderAttribute( request.getParameter( parameterName ) );
                listMyLuteceAttributeId.add( attributeMapping.getId( ) );
                if ( AttributeMappingHome.findByPrimaryKey( attributeMapping.getId( ) ) != null )
                {
                    AttributeMappingHome.update( attributeMapping );
                }
                else
                {
                    AttributeMappingHome.create( attributeMapping );
                }
            }
        }
        ;
        for ( IAttribute myLuteceAttribute : _listMyLuteceAttribute )
        {
            int nIdMyLuteceAttribute = myLuteceAttribute.getIdAttribute( );
            if ( !listMyLuteceAttributeId.contains( nIdMyLuteceAttribute ) )
            {
                AttributeMappingHome.remove( nIdMyLuteceAttribute );
            }
        }
        addInfo( INFO_ATTRIBUTE_MAPPING_DONE, getLocale( ) );
        return redirectView( request, VIEW_MANAGE_ATTRIBUTE_MAPPING );
    }

    /**
     * Returns the form to create a myLuteceSearchUser
     *
     * @param request
     *            The Http request
     * @return the html code of the myLuteceSearchUser form
     */
    @View( VIEW_CREATE_SEARCHUSER )
    public String getCreateMyLuteceSearchUser( HttpServletRequest request )
    {
        _myLuteceSearchUser = ( _myLuteceSearchUser != null ) ? _myLuteceSearchUser : new MyLuteceSearchUser( );
        Map<String, Object> model = getModel( );
        model.put( MARK_SEARCHUSER, _myLuteceSearchUser );
        if ( _listMyLuteceAttribute == null )
        {
            _listMyLuteceAttribute = gettMyLuteceAttributeWithFields( AttributeHome.findAll( _locale, _myLutecePlugin ) );
        }
        model.put( MARK_MYLUTECE_ATTRIBUTES_LIST, _listMyLuteceAttribute );
        return getPage( PROPERTY_PAGE_TITLE_CREATE_SEARCHUSER, TEMPLATE_CREATE_SEARCHUSER, model );
    }

    /**
     * Process the data capture form of a new myLuteceSearchUser
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_SEARCHUSER )
    public String doCreateMyLuteceSearchUser( HttpServletRequest request )
    {
        _myLuteceSearchUser = ( _myLuteceSearchUser == null ) ? new MyLuteceSearchUser( ) : _myLuteceSearchUser;
        if ( _listMyLuteceAttribute == null )
        {
            _listMyLuteceAttribute = gettMyLuteceAttributeWithFields( AttributeHome.findAll( _locale, _myLutecePlugin ) );
        }
        populate( _myLuteceSearchUser, request, request.getLocale( ) );
        setMyLuteceAttributeValue( request, _myLuteceSearchUser );
        // Check constraints
        if ( !validateBean( _myLuteceSearchUser, VALIDATION_ATTRIBUTES_PREFIX ) || !validateMyLuteceAttribute( request ) )
        {
            return redirectView( request, VIEW_CREATE_SEARCHUSER );
        }
        if ( MyLuteceSearchUserHome.findByConnectId( _myLuteceSearchUser.getProviderUserId( ) ) == null )
        {
            MyLuteceSearchUserHome.create( _myLuteceSearchUser );
        }
        else
        {
            addError( ERROR_SEARCHUSER_PROVIDER_USER_ID_EXIST, getLocale( ) );
            return redirectView( request, request.getParameter( PARAMETER_FROM_PROVIDER ) == null ? VIEW_CREATE_SEARCHUSER : VIEW_IMPORT_SEARCHUSER );
        }
        MyLuteceUserFieldService.doCreateUserFields( _myLuteceSearchUser.getId( ), request, request.getLocale( ) );
        addInfo( INFO_SEARCHUSER_CREATED, getLocale( ) );
        return redirectView( request, VIEW_MANAGE_SEARCHUSERS );
    }

    /**
     * Manages the removal form of a myLuteceSearchUser whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_SEARCHUSER )
    public String getConfirmRemoveMyLuteceSearchUser( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_SEARCHUSER ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_SEARCHUSER ) );
        url.addParameter( PARAMETER_ID_SEARCHUSER, nId );
        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_SEARCHUSER, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );
        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a myLuteceSearchUser
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage myLuteceSearchUsers
     */
    @Action( ACTION_REMOVE_SEARCHUSER )
    public String doRemoveMyLuteceSearchUser( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_SEARCHUSER ) );
        MyLuteceSearchUserHome.remove( nId );
        addInfo( INFO_SEARCHUSER_REMOVED, getLocale( ) );
        return redirectView( request, VIEW_MANAGE_SEARCHUSERS );
    }

    /**
     * Returns the form to update info about a myLuteceSearchUser
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_SEARCHUSER )
    public String getModifyMyLuteceSearchUser( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_SEARCHUSER ) );
        if ( _myLuteceSearchUser == null || ( _myLuteceSearchUser.getId( ) != nId ) )
        {
            _myLuteceSearchUser = MyLuteceSearchUserHome.findByPrimaryKey( nId );
        }
        if ( _listMyLuteceAttribute == null )
        {
            _listMyLuteceAttribute = gettMyLuteceAttributeWithFields( AttributeHome.findAll( _locale, _myLutecePlugin ) );
        }
        for ( IAttribute attribute : _listMyLuteceAttribute )
        {
            List<MyLuteceUserField> listUserFields = MyLuteceUserFieldHome.selectUserFieldsByIdUserIdAttribute( _myLuteceSearchUser.getId( ),
                    attribute.getIdAttribute( ), _myLutecePlugin );
            for ( AttributeField attributeField : attribute.getListAttributeFields( ) )
            {
                MyLuteceUserField myLuteceUserField = listUserFields.stream( ).limit( 1 )
                        .filter( userField -> userField.getAttribute( ).getIdAttribute( ) == attributeField.getAttribute( ).getIdAttribute( ) ).findAny( )
                        .orElse( null );
                if ( myLuteceUserField != null )
                {
                    attribute.getListAttributeFields( ).get( 0 ).setValue( myLuteceUserField.getValue( ) );
                }
            }
        }
        Map<String, Object> model = getModel( );
        model.put( MARK_SEARCHUSER, _myLuteceSearchUser );
        model.put( MARK_MYLUTECE_ATTRIBUTES_LIST, _listMyLuteceAttribute );
        return getPage( PROPERTY_PAGE_TITLE_MODIFY_SEARCHUSER, TEMPLATE_MODIFY_SEARCHUSER, model );
    }

    /**
     * Process the change form of a myLuteceSearchUser
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_SEARCHUSER )
    public String doModifyMyLuteceSearchUser( HttpServletRequest request )
    {
        populate( _myLuteceSearchUser, request, request.getLocale( ) );
        setMyLuteceAttributeValue( request, _myLuteceSearchUser );
        if ( !validateBean( _myLuteceSearchUser, VALIDATION_ATTRIBUTES_PREFIX ) || !validateMyLuteceAttribute( request ) )
        {
            return redirect( request, VIEW_MODIFY_SEARCHUSER, PARAMETER_ID_SEARCHUSER, _myLuteceSearchUser.getId( ) );
        }
        MyLuteceSearchUserHome.update( _myLuteceSearchUser );
        MyLuteceUserFieldService.doModifyUserFields( _myLuteceSearchUser.getId( ), request, request.getLocale( ), AdminUserService.getAdminUser( request ) );
        addInfo( INFO_SEARCHUSER_UPDATED, getLocale( ) );
        return redirectView( request, VIEW_MANAGE_SEARCHUSERS );
    }

    /**
     * Get a page to import users from Provider.
     * 
     * @param request
     *            The request
     * @return The HTML content
     */
    @View( VIEW_IMPORT_SEARCHUSER )
    public String getImportMyLuteceSearchUser( HttpServletRequest request )
    {
        _listAttributeMapping = ( _listAttributeMapping != null ) ? _listAttributeMapping : AttributeMappingHome.getAttributeMappingsList( );
        _listMyLuteceAttribute = ( _listMyLuteceAttribute != null ) ? _listMyLuteceAttribute
                : gettMyLuteceAttributeWithFields( AttributeHome.findAll( _locale, _myLutecePlugin ) );
        Map<String, Object> model = getModel( );
        setPageTitleProperty( PROPERTY_IMPORT_USERS_PAGETITLE );
        model.put( MARK_MYLUTECE_ATTRIBUTES_LIST, _listMyLuteceAttribute );
        model.put( MARK_ATTRIBUTE_MAPPING_LIST, _listAttributeMapping );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_IMPORT_USERS_FROM_PROVIDER, AdminUserService.getLocale( request ), model );
        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Get a page to import users from Provider.
     * 
     * @param request
     *            The request
     * @return The HTML content
     */
    @Action( ACTION_SEARCH_SEARCHUSER )
    public String doSearchUsersFromProvider( HttpServletRequest request )
    {
        String strParameterLastName = request.getParameter( PARAMETER_SEARCH_BY_LAST_NAME );
        String strParameterGivenName = request.getParameter( PARAMETER_SEARCH_BY_GIVEN_NAME );
        String strParameterEmail = request.getParameter( PARAMETER_SEARCH_BY_EMAIL );
        ReferenceList listProviderAttribute = new ReferenceList( );
        List<IAttribute> listAttributes = AttributeHome.findAll( getLocale( ), _myLutecePlugin );
        Enumeration<String> requestAttributes = request.getParameterNames( );
        while ( requestAttributes.hasMoreElements( ) )
        {
            String parameterName = requestAttributes.nextElement( );
            if ( parameterName.startsWith( PREFIX_PROVIDER_ATTRIBUTE ) )
            {
                String strProviderAttributeId = parameterName.substring( parameterName.lastIndexOf( "_" ) + 1 );
                ReferenceItem providerAttribute = new ReferenceItem( );
                providerAttribute.setName( strProviderAttributeId );
                providerAttribute.setCode( request.getParameter( parameterName ) );
                listProviderAttribute.add( providerAttribute );
            }
        }
        ;
        for ( IAttribute attribute : listAttributes )
        {
            List<AttributeField> listAttributeFields = AttributeFieldHome.selectAttributeFieldsByIdAttribute( attribute.getIdAttribute( ), _myLutecePlugin );
            attribute.setListAttributeFields( listAttributeFields );
        }
        List<MyLuteceSearchUser> users = MyLuteceUserSearchService.getInstance( ).findUsers( strParameterLastName, strParameterGivenName, strParameterEmail,
                listProviderAttribute );
        Map<String, Object> model = getModel( );
        model.put( MARK_MYLUTECE_ATTRIBUTES_LIST, _listMyLuteceAttribute );
        model.put( MARK_ATTRIBUTE_MAPPING_LIST, _listAttributeMapping );
        model.put( MARK_SEARCHUSER_LIST, users );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_IMPORT_USERS_FROM_PROVIDER, AdminUserService.getLocale( request ), model );
        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Validate Attribute from Mylutece
     *
     * @param request
     *            The Http request
     * @return returns false if one is not checked
     * 
     */
    private boolean validateMyLuteceAttribute( HttpServletRequest request )
    {
        List<ErrorMessage> listErrors = new ArrayList<>( );
        for ( IAttribute attribute : _listMyLuteceAttribute )
        {
            if ( attribute.isMandatory( ) && attribute.getListAttributeFields( ).get( 0 ).getValue( ).isEmpty( ) )
            {
                MVCMessage error = new MVCMessage( );
                error.setMessage( attribute.getTitle( ) + " " + I18nService.getLocalizedString( MESSAGE_FIELD_ERROR_MANDATORY, request.getLocale( ) ) );
                listErrors.add( error );
            }
        }
        if ( !listErrors.isEmpty( ) )
        {
            for ( ErrorMessage errorMessage : listErrors )
            {
                String strMessageErr = errorMessage.getMessage( );
                addError( strMessageErr );
            }
            return false;
        }
        return true;
    }

    /**
     * Get a list of MyLutece attribute
     *
     * @return returns a list of mylutece attribute
     * 
     */
    private List<IAttribute> gettMyLuteceAttributeWithFields( List<IAttribute> listMyLuteceAttribute )
    {
        for ( IAttribute attribute : listMyLuteceAttribute )
        {
            List<AttributeField> listAttributeFields = AttributeFieldHome.selectAttributeFieldsByIdAttribute( attribute.getIdAttribute( ), _myLutecePlugin );
            attribute.setListAttributeFields( listAttributeFields );
        }
        return listMyLuteceAttribute;
    }

    /**
     * Fill mylutece attribute in request & searchUser
     *
     * @param request
     *            The Http request
     * @param searchUser
     *            The Http request
     * @return returns a list of mylutece attribute
     * 
     */
    private void setMyLuteceAttributeValue( HttpServletRequest request, MyLuteceSearchUser myLuteceSearchUser )
    {
        ReferenceList userAttributes = new ReferenceList( );
        for ( IAttribute attribute : _listMyLuteceAttribute )
        {
            String strAttributeName = PARAMETER_MYLUTECE_ATTRIBUTE_NAME + "_" + attribute.getIdAttribute( );
            String strAttributeValue = request.getParameter( strAttributeName );
            if ( strAttributeValue != null )
            {
                ReferenceItem userAttribute = new ReferenceItem( );
                userAttribute.setCode( strAttributeValue );
                userAttribute.setName( String.valueOf( attribute.getIdAttribute( ) ) );
                attribute.getListAttributeFields( ).get( 0 ).setValue( strAttributeValue );
            }
        }
        myLuteceSearchUser.setAttributes( userAttributes );
    }

}
