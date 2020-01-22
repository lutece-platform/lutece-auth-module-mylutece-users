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
import fr.paris.lutece.plugins.mylutece.modules.users.business.LocalUser;
import fr.paris.lutece.plugins.mylutece.modules.users.business.LocalUserHome;
import fr.paris.lutece.plugins.mylutece.modules.users.service.LocalUserInfoService;
import fr.paris.lutece.plugins.mylutece.service.MyLutecePlugin;
import fr.paris.lutece.plugins.mylutece.service.attribute.MyLuteceUserFieldService;
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
 * This class provides the user interface to manage LocalUser features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageLocalUsers.jsp", controllerPath = "jsp/admin/plugins/mylutece/modules/users/", right = "MYLUTECE_USERS_MANAGEMENT" )
public class LocalUserJspBean extends AbstractmyLuteceUsersManagementJspBean
{
    /**
     *
     */
    private static final long serialVersionUID = 2163559576736974617L;
    // Templates
    private static final String TEMPLATE_MANAGE_LOCALUSERS = "/admin/plugins/mylutece/modules/users/manage_localusers.html";
    private static final String TEMPLATE_CREATE_LOCALUSER = "/admin/plugins/mylutece/modules/users/create_localuser.html";
    private static final String TEMPLATE_MODIFY_LOCALUSER = "/admin/plugins/mylutece/modules/users/modify_localuser.html";
    private static final String TEMPLATE_IMPORT_USERS_FROM_PROVIDER = "/admin/plugins/mylutece/modules/users/import_users_from_provider.html";
    private static final String TEMPLATE_MANAGE_ATTRIBUTE_MAPPING = "/admin/plugins/mylutece/modules/users/manage_attribute_mapping.html";
    // Parameters
    private static final String PARAMETER_ID_LOCALUSER = "id";
    private static final String PARAMETER_MYLUTECE_ATTRIBUTE_NAME = "attribute";
    private static final String PARAMETER_SEARCH_BY_LAST_NAME = "search_lastName";
    private static final String PARAMETER_SEARCH_BY_GIVEN_NAME = "search_givenName";
    private static final String PARAMETER_SEARCH_BY_EMAIL = "search_email";
    private static final String PARAMETER_FROM_PROVIDER = "import_from_provider";
    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_LOCALUSERS = "module.mylutece.users.manage_localusers.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_LOCALUSER = "module.mylutece.users.modify_localuser.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_LOCALUSER = "module.mylutece.users.create_localuser.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MANAGE_ATTRIBUTE_MAPPING = "module.mylutece.users.manage_attribute_mapping.pageTitle";
    private static final String PROPERTY_IMPORT_USERS_PAGETITLE = "module.mylutece.users.import.pageTitle";
    // Markers
    private static final String MARK_LOCALUSER_LIST = "localuser_list";
    private static final String MARK_LOCALUSER = "localuser";
    private static final String MARK_MYLUTECE_ATTRIBUTES_LIST = "attribute_list";
    private static final String MARK_PROVIDER_ATTRIBUTES_LIST = "provider_attribute_list";
    private static final String MARK_ATTRIBUTE_MAPPING_LIST = "attribute_mapping_list";
    // Jsp
    private static final String JSP_MANAGE_LOCALUSERS = "jsp/admin/plugins/mylutece/modules/users/ManageLocalUsers.jsp";
    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_LOCALUSER = "module.mylutece.users.message.confirmRemoveLocalUser";
    private static final String MESSAGE_FIELD_ERROR_MANDATORY = "module.mylutece.users.message.field.error.mandatory";
    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "module.mylutece.users.model.entity.localuser.attribute.";
    // Views
    private static final String VIEW_MANAGE_LOCALUSERS = "manageLocalUsers";
    private static final String VIEW_CREATE_LOCALUSER = "createLocalUser";
    private static final String VIEW_MODIFY_LOCALUSER = "modifyLocalUser";
    private static final String VIEW_IMPORT_LOCALUSER = "importLocalUser";
    private static final String VIEW_MANAGE_ATTRIBUTE_MAPPING = "manageAttributeMapping";
    // Actions
    private static final String ACTION_CREATE_LOCALUSER = "createLocalUser";
    private static final String ACTION_MODIFY_LOCALUSER = "modifyLocalUser";
    private static final String ACTION_REMOVE_LOCALUSER = "removeLocalUser";
    private static final String ACTION_CONFIRM_REMOVE_LOCALUSER = "confirmRemoveLocalUser";
    private static final String ACTION_SEARCH_LOCALUSER = "searchUsersFromProvider";
    private static final String ACTION_UPDATE_ATTRIBUTE_MAPPING = "updateAttributeMapping";
    // Infos
    private static final String INFO_LOCALUSER_CREATED = "module.mylutece.users.info.localuser.created";
    private static final String INFO_LOCALUSER_UPDATED = "module.mylutece.users.info.localuser.updated";
    private static final String INFO_LOCALUSER_REMOVED = "module.mylutece.users.info.localuser.removed";
    private static final String ERROR_LOCALUSER_PROVIDER_USER_ID_EXIST = "module.mylutece.users.error.localuser.providerUserIdExist";
    private static final String INFO_ATTRIBUTE_MAPPING_DONE = "module.mylutece.users.info.attribute_mapping.done";
    // Prefixes
    private static final String PREFIX_ATTRIBUTE_MAPPING = "attribute_mapping_";
    private static final String PREFIX_PROVIDER_ATTRIBUTE = "provider_attribute_";
    // Session variables
    private LocalUser _localuser;
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
    @View( value = VIEW_MANAGE_LOCALUSERS, defaultView = true )
    public String getManageLocalUsers( HttpServletRequest request )
    {
        _localuser = null;
        _listMyLuteceAttribute = null;
        List<LocalUser> listLocalUsers = LocalUserHome.getLocalUsersList( );
        Map<String, Object> model = getPaginatedListModel( request, MARK_LOCALUSER_LIST, listLocalUsers, JSP_MANAGE_LOCALUSERS );
        return getPage( PROPERTY_PAGE_TITLE_MANAGE_LOCALUSERS, TEMPLATE_MANAGE_LOCALUSERS, model );
    }

    /**
     * Returns the form to create a localuser
     *
     * @param request
     *            The Http request
     * @return the html code of the localuser form
     */
    @View( VIEW_MANAGE_ATTRIBUTE_MAPPING )
    public String getManageAttributeMapping( HttpServletRequest request )
    {
        _listMyLuteceAttribute = gettMyLuteceAttributeWithFields( AttributeHome.findAll( _locale, _myLutecePlugin ) );
        _listProviderAttribute = LocalUserInfoService.getInstance( ).getAllAttributes( );
        _listAttributeMapping = AttributeMappingHome.getAttributeMappingsList( );
        Map<String, Object> model = getModel( );
        model.put( MARK_LOCALUSER, _localuser );
        model.put( MARK_ATTRIBUTE_MAPPING_LIST, _listAttributeMapping );
        model.put( MARK_MYLUTECE_ATTRIBUTES_LIST, _listMyLuteceAttribute );
        model.put( MARK_PROVIDER_ATTRIBUTES_LIST, _listProviderAttribute );
        return getPage( PROPERTY_PAGE_TITLE_MANAGE_ATTRIBUTE_MAPPING, TEMPLATE_MANAGE_ATTRIBUTE_MAPPING, model );
    }

    /**
     * Process the data capture form of a new localuser
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
     * Returns the form to create a localuser
     *
     * @param request
     *            The Http request
     * @return the html code of the localuser form
     */
    @View( VIEW_CREATE_LOCALUSER )
    public String getCreateLocalUser( HttpServletRequest request )
    {
        _localuser = ( _localuser != null ) ? _localuser : new LocalUser( );
        Map<String, Object> model = getModel( );
        model.put( MARK_LOCALUSER, _localuser );
        if ( _listMyLuteceAttribute == null )
        {
            _listMyLuteceAttribute = gettMyLuteceAttributeWithFields( AttributeHome.findAll( _locale, _myLutecePlugin ) );
        }
        model.put( MARK_MYLUTECE_ATTRIBUTES_LIST, _listMyLuteceAttribute );
        return getPage( PROPERTY_PAGE_TITLE_CREATE_LOCALUSER, TEMPLATE_CREATE_LOCALUSER, model );
    }

    /**
     * Process the data capture form of a new localuser
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_LOCALUSER )
    public String doCreateLocalUser( HttpServletRequest request )
    {
        _localuser = ( _localuser == null ) ? new LocalUser( ) : _localuser;
        if ( _listMyLuteceAttribute == null )
        {
            _listMyLuteceAttribute = gettMyLuteceAttributeWithFields( AttributeHome.findAll( _locale, _myLutecePlugin ) );
        }
        populate( _localuser, request, request.getLocale( ) );
        setMyLuteceAttributeValue( request );
        // Check constraints
        if ( !validateBean( _localuser, VALIDATION_ATTRIBUTES_PREFIX ) || !validateMyLuteceAttribute( request ) )
        {
            return redirectView( request, VIEW_CREATE_LOCALUSER );
        }
        if ( LocalUserHome.findByConnectId( _localuser.getProviderUserId( ) ) == null )
        {
            LocalUserHome.create( _localuser );
        }
        else
        {
            addError( ERROR_LOCALUSER_PROVIDER_USER_ID_EXIST, getLocale( ) );
            return redirectView( request, request.getParameter( PARAMETER_FROM_PROVIDER ) == null ? VIEW_CREATE_LOCALUSER : VIEW_IMPORT_LOCALUSER );
        }
        MyLuteceUserFieldService.doCreateUserFields( _localuser.getId( ), request, request.getLocale( ) );
        addInfo( INFO_LOCALUSER_CREATED, getLocale( ) );
        return redirectView( request, VIEW_MANAGE_LOCALUSERS );
    }

    /**
     * Manages the removal form of a localuser whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_LOCALUSER )
    public String getConfirmRemoveLocalUser( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_LOCALUSER ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_LOCALUSER ) );
        url.addParameter( PARAMETER_ID_LOCALUSER, nId );
        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_LOCALUSER, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );
        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a localuser
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage localusers
     */
    @Action( ACTION_REMOVE_LOCALUSER )
    public String doRemoveLocalUser( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_LOCALUSER ) );
        LocalUserHome.remove( nId );
        addInfo( INFO_LOCALUSER_REMOVED, getLocale( ) );
        return redirectView( request, VIEW_MANAGE_LOCALUSERS );
    }

    /**
     * Returns the form to update info about a localuser
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_LOCALUSER )
    public String getModifyLocalUser( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_LOCALUSER ) );
        if ( _localuser == null || ( _localuser.getId( ) != nId ) )
        {
            _localuser = LocalUserHome.findByPrimaryKey( nId );
        }
        if ( _listMyLuteceAttribute == null )
        {
            _listMyLuteceAttribute = gettMyLuteceAttributeWithFields( AttributeHome.findAll( _locale, _myLutecePlugin ) );
        }
        for ( IAttribute attribute : _listMyLuteceAttribute )
        {
            List<MyLuteceUserField> listUserFields = MyLuteceUserFieldHome.selectUserFieldsByIdUserIdAttribute( _localuser.getId( ),
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
        model.put( MARK_LOCALUSER, _localuser );
        model.put( MARK_MYLUTECE_ATTRIBUTES_LIST, _listMyLuteceAttribute );
        return getPage( PROPERTY_PAGE_TITLE_MODIFY_LOCALUSER, TEMPLATE_MODIFY_LOCALUSER, model );
    }

    /**
     * Process the change form of a localuser
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_LOCALUSER )
    public String doModifyLocalUser( HttpServletRequest request )
    {
        populate( _localuser, request, request.getLocale( ) );
        setMyLuteceAttributeValue( request );
        if ( !validateBean( _localuser, VALIDATION_ATTRIBUTES_PREFIX ) || !validateMyLuteceAttribute( request ) )
        {
            return redirect( request, VIEW_MODIFY_LOCALUSER, PARAMETER_ID_LOCALUSER, _localuser.getId( ) );
        }
        LocalUserHome.update( _localuser );
        MyLuteceUserFieldService.doModifyUserFields( _localuser.getId( ), request, request.getLocale( ), AdminUserService.getAdminUser( request ) );
        addInfo( INFO_LOCALUSER_UPDATED, getLocale( ) );
        return redirectView( request, VIEW_MANAGE_LOCALUSERS );
    }

    /**
     * Get a page to import users from Provider.
     * 
     * @param request
     *            The request
     * @return The HTML content
     */
    @View( VIEW_IMPORT_LOCALUSER )
    public String getImportLocalUser( HttpServletRequest request )
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
    @Action( ACTION_SEARCH_LOCALUSER )
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
        List<LocalUser> users = LocalUserInfoService.getInstance( ).findUsers( strParameterLastName, strParameterGivenName, strParameterEmail,
                listProviderAttribute );
        Map<String, Object> model = getModel( );
        model.put( MARK_MYLUTECE_ATTRIBUTES_LIST, _listMyLuteceAttribute );
        model.put( MARK_ATTRIBUTE_MAPPING_LIST, _listAttributeMapping );
        model.put( MARK_LOCALUSER_LIST, users );
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
     * Fill mylutece attribute from user request
     *
     * @param request
     *            The Http request
     * @return returns a list of mylutece attribute
     * 
     */
    private void setMyLuteceAttributeValue( HttpServletRequest request )
    {
        for ( IAttribute attribute : _listMyLuteceAttribute )
        {
            String strAttributeName = PARAMETER_MYLUTECE_ATTRIBUTE_NAME + "_" + attribute.getIdAttribute( );
            String strAttributeValue = request.getParameter( strAttributeName );
            if ( strAttributeValue != null )
            {
                attribute.getListAttributeFields( ).get( 0 ).setValue( strAttributeValue );
            }
        }
    }

}
