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
package fr.paris.lutece.plugins.mylutece.modules.users.service.search;

import fr.paris.lutece.plugins.mylutece.modules.users.business.LocalUser;
import fr.paris.lutece.plugins.mylutece.modules.users.business.LocalUserHome;
import fr.paris.lutece.portal.service.content.XPageAppService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.search.IndexationService;
import fr.paris.lutece.portal.service.search.SearchIndexer;
import fr.paris.lutece.portal.service.search.SearchItem;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.url.UrlItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;

/**
 * Indexer service for localUser Xpages
 */
public class LocalUserIndexer implements SearchIndexer
{
    public static final String SHORT_NAME = "mlu";
    private static final String ENABLE_VALUE_TRUE = "1";
    public static final String PROPERTY_INDEXER_NAME = "mylutece-users.indexer.name";
    private static final String PROPERTY_INDEXER_DESCRIPTION = "mylutece-users.indexer.description";
    private static final String PROPERTY_INDEXER_VERSION = "mylutece-users.indexer.version";
    private static final String PROPERTY_INDEXER_ENABLE = "mylutece-users.indexer.enable";
    public static final String PROPERTY_INDEX_TYPE_PAGE = "localUser";
    private static final String PARAMETER_LOCALUSER_ID = "localUser_id";
    private static final String JSP_SEARCH_LOCALUSER = "";
    public static final String FIELD_ID_TITLE = "id";
    public static final String FIELD_LOGIN_TITLE = "login";
    public static final String FIELD_GIVEN_NAME_TITLE = "givenName";
    public static final String FIELD_LAST_NAME_TITLE = "LastName";
    public static final String FIELD_EMAIL_TITLE = "email";
    String _pluginName = "mylutece-users";

    /**
     * Index all Local Users
     * 
     * @throws IOException
     *             exception
     * @throws InterruptedException
     *             exception
     * @throws SiteMessageException
     *             exception
     */
    public void indexDocuments( ) throws IOException, InterruptedException, SiteMessageException
    {
        String strPortalUrl = AppPathService.getPortalUrl( );
        Plugin plugin = PluginService.getPlugin( _pluginName );
        List<LocalUser> listLocalUsers = LocalUserHome.getLocalUsersList( );
        for ( LocalUser localUser : listLocalUsers )
        {
            UrlItem url = new UrlItem( strPortalUrl );
            url.addParameter( XPageAppService.PARAM_XPAGE_APP, _pluginName );
            url.addParameter( PARAMETER_LOCALUSER_ID, localUser.getId( ) );
            org.apache.lucene.document.Document docLocalUser = getDocument( localUser, plugin );
            IndexationService.write( docLocalUser );
        }
    }

    /**
     * Return a list of lucene document for incremental indexing
     * 
     * @param strId
     *            Document id
     * @return listDocuments the document list
     */
    public List<Document> getDocuments( String strId ) throws IOException, InterruptedException, SiteMessageException
    {
        ArrayList<org.apache.lucene.document.Document> listDocuments = new ArrayList<>( );
        String strPortalUrl = AppPathService.getPortalUrl( );
        Plugin plugin = PluginService.getPlugin( _pluginName );
        LocalUser localUser = LocalUserHome.findByPrimaryKey( Integer.parseInt( strId ) );
        if ( localUser != null )
        {
            UrlItem url = new UrlItem( strPortalUrl );
            url.addParameter( XPageAppService.PARAM_XPAGE_APP, _pluginName );
            url.addParameter( PARAMETER_LOCALUSER_ID, localUser.getId( ) );
            org.apache.lucene.document.Document docLocalUser = null;
            try
            {
                docLocalUser = getDocument( localUser, plugin );
            }
            catch( Exception e )
            {
                String strMessage = "LocalUser ID : " + localUser.getId( );
                IndexationService.error( this, e, strMessage );
            }
            if ( docLocalUser != null )
            {
                listDocuments.add( docLocalUser );
            }
        }
        return listDocuments;
    }

    /**
     * {@inheritDoc}
     */
    public String getName( )
    {
        return AppPropertiesService.getProperty( PROPERTY_INDEXER_NAME );
    }

    /**
     * {@inheritDoc}
     */
    public String getVersion( )
    {
        return AppPropertiesService.getProperty( PROPERTY_INDEXER_VERSION );
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEnable( )
    {
        boolean bReturn = false;
        String strEnable = AppPropertiesService.getProperty( PROPERTY_INDEXER_ENABLE );
        if ( ( strEnable != null ) && ( strEnable.equalsIgnoreCase( Boolean.TRUE.toString( ) ) || strEnable.equals( ENABLE_VALUE_TRUE ) )
                && PluginService.isPluginEnable( _pluginName ) )
        {
            bReturn = true;
        }
        return bReturn;
    }

    /**
     * Returns the indexer service description
     * 
     * @return The indexer service description
     */
    public String getDescription( )
    {
        return AppPropertiesService.getProperty( PROPERTY_INDEXER_DESCRIPTION );
    }

    /**
     * Builds a document which will be used by Lucene during the indexing of the pages of the site with the following fields : summary, uid, url, contents,
     * title and description.
     * 
     * @return the built Document
     * @param strUrl
     *            The base URL for documents
     * @param localUser
     *            the page to index
     * @param plugin
     *            The {@link Plugin}
     * @throws IOException
     *             The IO Exception
     * @throws InterruptedException
     *             The InterruptedException
     * @throws SiteMessageException
     *             occurs when a site message need to be displayed
     */
    private Document getDocument( LocalUser localUser, Plugin plugin ) throws IOException, InterruptedException, SiteMessageException
    {
        // make a new, empty document
        org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document( );
        doc.add( new Field( SearchItem.FIELD_CONTENTS, getContentToIndex( localUser ), TextField.TYPE_NOT_STORED ) );
        doc.add( new Field( SearchItem.FIELD_UID, String.valueOf( localUser.getId( ) ), TextField.TYPE_STORED ) );
        doc.add( new Field( SearchItem.FIELD_TYPE, _pluginName, TextField.TYPE_STORED ) );
        doc.add( new Field( SearchItem.FIELD_TITLE, getFullName( localUser ), TextField.TYPE_STORED ) );
        ReferenceList listAttribute = localUser.getAttributes( );

        if ( listAttribute != null )
        {
            for ( ReferenceItem attribute : listAttribute )
            {
                doc.add( new Field( "attribute_" + attribute.getName( ), attribute.getCode( ), TextField.TYPE_STORED ) );
            }
        }

        return doc;
    }

    /**
     * Set the Content to index
     * 
     * @param localUser
     *            The localUser to index
     * @return The content to index
     */
    private static String getContentToIndex( LocalUser localUser )
    {
        StringBuilder sbContentToIndex = new StringBuilder( );
        sbContentToIndex.append( localUser.getLogin( ) );
        sbContentToIndex.append( " " );
        sbContentToIndex.append( localUser.getGivenName( ) );
        sbContentToIndex.append( " " );
        sbContentToIndex.append( localUser.getLastName( ) );
        sbContentToIndex.append( " " );
        sbContentToIndex.append( localUser.getEmail( ) );
        return sbContentToIndex.toString( );
    }

    /**
     * Set the Content to index
     * 
     * @param localUser
     *            The localUser to index
     * @return The content to index
     */
    private static String getFullName( LocalUser localUser )
    {
        StringBuilder sbContentToIndex = new StringBuilder( );
        sbContentToIndex.append( localUser.getLastName( ) );
        sbContentToIndex.append( " " );
        sbContentToIndex.append( localUser.getGivenName( ) );
        return sbContentToIndex.toString( );
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getListType( )
    {
        List<String> listType = new ArrayList<>( );
        listType.add( _pluginName );
        return listType;
    }

    /**
     * {@inheritDoc}
     */
    public String getSpecificSearchAppUrl( )
    {
        return JSP_SEARCH_LOCALUSER;
    }
}
