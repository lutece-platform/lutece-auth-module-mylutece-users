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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.mylutece.service.search.MyLuteceSearchUser;
import fr.paris.lutece.portal.business.role.Role;
import fr.paris.lutece.portal.service.template.HtmlAttributesMethod;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.TemplateMethodModelEx;

/**
 * Renders the screen assigning roles to a user with the core macros and the HTML auto-escaping (LUT-33153): the filter
 * field and the role check boxes render with htmlAttributes strict, and the values of the user and of the roles are
 * written as text.
 */
public class MyLuteceUserRoleTemplatesRenderTest
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";
    private static final String CORE_TEMPLATES_ROOT = "target/lutece/WEB-INF/templates";
    private static final String COMMONS = "commons_bs5_tabler.html";
    private static final String TEMPLATE_MANAGE_USER_ROLES = "admin/plugins/mylutece/modules/users/manage_user_roles.html";

    private static final String ROLE_KEY = "ae_users_role";
    private static final String HOSTILE = "Name <b>x</b> \"q\"";
    private static final String HOSTILE_ESCAPED = "Name &lt;b&gt;x&lt;/b&gt; &quot;q&quot;";

    /**
     * Renders a template of the module with the configuration of the core: HTML output format, auto-escaping, the back
     * office commons included, htmlAttributes strict.
     *
     * @param strTemplate
     *            the template path
     * @param model
     *            the model
     * @return the HTML
     * @throws Exception
     *             if the rendering fails
     */
    private static String render( String strTemplate, Map<String, Object> model ) throws Exception
    {
        File coreTemplates = new File( CORE_TEMPLATES_ROOT );
        assertTrue( new File( coreTemplates, COMMONS ).isFile( ), "core templates not found under " + CORE_TEMPLATES_ROOT + ": run lutece:exploded first" );
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_0 );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        cfg.setLocale( Locale.FRENCH );
        cfg.setURLEscapingCharset( "UTF-8" );
        cfg.setNumberFormat( "0.######" );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] {
                new FileTemplateLoader( new File( TEMPLATES_ROOT ) ), new FileTemplateLoader( coreTemplates )
        } ) );
        cfg.addAutoInclude( COMMONS );
        cfg.setSharedVariable( "htmlAttributes", new HtmlAttributesMethod( true ) );
        cfg.setSharedVariable( "i18n", (TemplateMethodModelEx) arguments -> String.valueOf( arguments.get( 0 ) ) );
        StringWriter writer = new StringWriter( );
        cfg.getTemplate( strTemplate ).process( model, writer );

        return writer.toString( );
    }

    /**
     * Builds a role.
     *
     * @param strKey
     *            the role key
     * @param strDescription
     *            the role description
     * @return the role
     */
    private static Role role( String strKey, String strDescription )
    {
        Role role = new Role( );
        role.setRole( strKey );
        role.setRoleDescription( strDescription );

        return role;
    }

    /**
     * The roles screen renders (it showed an internal error while the filter field got its attributes as a string): the
     * filter field gets autocomplete="off", each role check box its data-role, the role held by the user is checked, and
     * the names of the user and the description of the role are written as text.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testManageUserRoles( ) throws Exception
    {
        MyLuteceSearchUser user = new MyLuteceSearchUser( );
        user.setId( 7 );
        user.setGivenName( HOSTILE );
        user.setLastName( HOSTILE );
        Role role = role( ROLE_KEY, HOSTILE );
        Map<String, Object> model = new HashMap<>( );
        model.put( "mylutece_search_user", user );
        model.put( "id_mylutecesearchuser", "7" );
        model.put( "mylutece_role_list", List.of( role( "none", "none" ), role ) );
        model.put( "mylutece_search_user_role_list", List.of( role ) );
        model.put( "infos", List.of( ) );

        String strHtml = render( TEMPLATE_MANAGE_USER_ROLES, model );

        assertTrue( strHtml.contains( "autocomplete=\"off\"" ), strHtml );
        assertTrue( strHtml.contains( "data-role=\"" + ROLE_KEY + " " + HOSTILE_ESCAPED + "\"" ), strHtml );
        assertTrue( strHtml.contains( "value=\"" + ROLE_KEY + "\"" ), strHtml );
        assertTrue( strHtml.contains( "checked" ), strHtml );
        assertTrue( strHtml.contains( HOSTILE_ESCAPED ), strHtml );
        assertFalse( strHtml.contains( "<b>x</b>" ), strHtml );
        assertFalse( strHtml.contains( "value=\"none\"" ), strHtml );
    }
}
