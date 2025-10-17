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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import fr.paris.lutece.plugins.mylutece.modules.users.business.MyLuteceSearchUserHome;
import fr.paris.lutece.plugins.mylutece.service.search.MyLuteceSearchUser;
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

/**
 * Indexer service for myLuteceSearchUser Xpages
 */
public class MyLuteceSearchUserIndexer implements SearchIndexer
{
    public static final String SHORT_NAME = "mlu";
    private static final String ENABLE_VALUE_TRUE = "1";
    public static final String PROPERTY_INDEXER_NAME = "mylutece-users.indexer.name";
    private static final String PROPERTY_INDEXER_DESCRIPTION = "mylutece-users.indexer.description";
    private static final String PROPERTY_INDEXER_VERSION = "mylutece-users.indexer.version";
    private static final String PROPERTY_INDEXER_ENABLE = "mylutece-users.indexer.enable";
    public static final String PROPERTY_INDEX_TYPE_PAGE = "myLuteceSearchUser";
    private static final String PARAMETER_SEARCHUSER_ID = "myLuteceSearchUser_id";
    private static final String JSP_SEARCH_SEARCHUSER = "";
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
        List<MyLuteceSearchUser> listMyLuteceSearchUsers = MyLuteceSearchUserHome.getMyLuteceSearchUsersList( );
        for ( MyLuteceSearchUser myLuteceSearchUser : listMyLuteceSearchUsers )
        {
            UrlItem url = new UrlItem( strPortalUrl );
            url.addParameter( XPageAppService.PARAM_XPAGE_APP, _pluginName );
            url.addParameter( PARAMETER_SEARCHUSER_ID, myLuteceSearchUser.getId( ) );
            org.apache.lucene.document.Document docMyLuteceSearchUser = getDocument( myLuteceSearchUser, plugin );
            IndexationService.write( docMyLuteceSearchUser );
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
        MyLuteceSearchUser myLuteceSearchUser = MyLuteceSearchUserHome.findByPrimaryKey( Integer.parseInt( strId ) );
        if ( myLuteceSearchUser != null )
        {
            UrlItem url = new UrlItem( strPortalUrl );
            url.addParameter( XPageAppService.PARAM_XPAGE_APP, _pluginName );
            url.addParameter( PARAMETER_SEARCHUSER_ID, myLuteceSearchUser.getId( ) );
            org.apache.lucene.document.Document docMyLuteceSearchUser = null;
            try
            {
                docMyLuteceSearchUser = getDocument( myLuteceSearchUser, plugin );
            }
            catch( Exception e )
            {
                String strMessage = "MyLuteceSearchUser ID : " + myLuteceSearchUser.getId( );
                IndexationService.error( this, e, strMessage );
            }
            if ( docMyLuteceSearchUser != null )
            {
                listDocuments.add( docMyLuteceSearchUser );
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
     * @param myLuteceSearchUser
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
    private Document getDocument( MyLuteceSearchUser myLuteceSearchUser, Plugin plugin ) throws IOException, InterruptedException, SiteMessageException
    {
        // make a new, empty document
        org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document( );
        doc.add( new Field( SearchItem.FIELD_CONTENTS, getContentToIndex( myLuteceSearchUser ), TextField.TYPE_NOT_STORED ) );
        doc.add( new StringField( SearchItem.FIELD_UID, String.valueOf( myLuteceSearchUser.getId( ) ), Field.Store.YES ) );
        doc.add( new StringField( SearchItem.FIELD_TYPE, _pluginName, Field.Store.YES ) );
        doc.add( new TextField( SearchItem.FIELD_TITLE, getFullName( myLuteceSearchUser ), Field.Store.YES ) );
        ReferenceList listAttribute = myLuteceSearchUser.getAttributes( );

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
     * @param myLuteceSearchUser
     *            The myLuteceSearchUser to index
     * @return The content to index
     */
    private static String getContentToIndex( MyLuteceSearchUser myLuteceSearchUser )
    {
        StringBuilder sbContentToIndex = new StringBuilder( );
        sbContentToIndex.append( myLuteceSearchUser.getLogin( ) );
        sbContentToIndex.append( " " );
        sbContentToIndex.append( myLuteceSearchUser.getGivenName( ) );
        sbContentToIndex.append( " " );
        sbContentToIndex.append( myLuteceSearchUser.getLastName( ) );
        sbContentToIndex.append( " " );
        sbContentToIndex.append( myLuteceSearchUser.getEmail( ) );
        return sbContentToIndex.toString( );
    }

    /**
     * Set the Content to index
     * 
     * @param myLuteceSearchUser
     *            The myLuteceSearchUser to index
     * @return The content to index
     */
    private static String getFullName( MyLuteceSearchUser myLuteceSearchUser )
    {
        StringBuilder sbContentToIndex = new StringBuilder( );
        sbContentToIndex.append( myLuteceSearchUser.getLastName( ) );
        sbContentToIndex.append( " " );
        sbContentToIndex.append( myLuteceSearchUser.getGivenName( ) );
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
        return JSP_SEARCH_SEARCHUSER;
    }
}
