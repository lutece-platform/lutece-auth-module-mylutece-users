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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.mylutece.service.search.MyLuteceSearchUser;
import fr.paris.lutece.portal.service.template.HtmlAttributesMethod;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.TemplateMethodModelEx;

/**
 * Renders the forms creating and modifying a user with the core macros: the Cancel button leaves the form without the
 * browser checking the mandatory fields, and goes back to the list of the users.
 */
public class MyLuteceSearchUserTemplatesRenderTest
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";
    private static final String CORE_TEMPLATES_ROOT = "target/lutece/WEB-INF/templates";
    private static final String COMMONS = "commons_bs5_tabler.html";
    private static final String PATH = "admin/plugins/mylutece/modules/users/";
    private static final String TEMPLATE_CREATE = PATH + "create_search_user.html";
    private static final String TEMPLATE_MODIFY = PATH + "modify_search_user.html";
    private static final String CANCEL = "name=\"view_manageMyLuteceSearchUsers\" formnovalidate";

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
     * The model of the user forms, as MyLuteceSearchUserJspBean builds it.
     *
     * @return the model
     */
    private static Map<String, Object> userModel( )
    {
        MyLuteceSearchUser user = new MyLuteceSearchUser( );
        user.setId( 7 );
        user.setLogin( "login" );
        user.setGivenName( "Given" );
        user.setLastName( "Last" );
        user.setEmail( "user@example.org" );
        Map<String, Object> model = new HashMap<>( );
        model.put( "mylutece_search_user", user );
        model.put( "attribute_list", List.of( ) );
        model.put( "errors", List.of( ) );

        return model;
    }

    /**
     * The Cancel button of the creation form skips the validation of the mandatory fields.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testCreateFormCancelSkipsValidation( ) throws Exception
    {
        String strHtml = render( TEMPLATE_CREATE, userModel( ) );

        assertTrue( strHtml.contains( "required" ), strHtml );
        assertTrue( strHtml.contains( CANCEL ), strHtml );
    }

    /**
     * The Cancel button of the modification form skips the validation of the mandatory fields.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testModifyFormCancelSkipsValidation( ) throws Exception
    {
        String strHtml = render( TEMPLATE_MODIFY, userModel( ) );

        assertTrue( strHtml.contains( "required" ), strHtml );
        assertTrue( strHtml.contains( CANCEL ), strHtml );
    }
}
