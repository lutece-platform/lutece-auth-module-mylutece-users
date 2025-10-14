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
package fr.paris.lutece.plugins.mylutece.modules.users.business;

import fr.paris.lutece.plugins.mylutece.service.search.MyLuteceSearchUser;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.search.IndexationService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.plugins.mylutece.business.attribute.AttributeField;
import fr.paris.lutece.plugins.mylutece.business.attribute.AttributeFieldHome;
import fr.paris.lutece.plugins.mylutece.business.attribute.AttributeHome;
import fr.paris.lutece.plugins.mylutece.business.attribute.IAttribute;
import fr.paris.lutece.plugins.mylutece.business.attribute.MyLuteceUserField;
import fr.paris.lutece.plugins.mylutece.business.attribute.MyLuteceUserFieldHome;
import fr.paris.lutece.plugins.mylutece.modules.users.service.search.MyLuteceSearchUserIndexer;
import fr.paris.lutece.plugins.mylutece.service.MyLutecePlugin;
import fr.paris.lutece.portal.business.indexeraction.IndexerAction;
import java.util.List;
import java.util.Locale;

/**
 * This class provides instances management methods (create, find, ...) for MyLuteceSearchUser objects
 */
public final class MyLuteceSearchUserHome
{
    // Static variable pointed at the DAO instance
    private static IMyLuteceSearchUserDAO _dao = SpringContextService.getBean( "mylutece-users.myLuteceSearchUserDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "mylutece-users" );
    private static Locale _locale;
    private static Plugin _myLutecePlugin = PluginService.getPlugin( MyLutecePlugin.PLUGIN_NAME );

    /**
     * Private constructor - this class need not be instantiated
     */
    private MyLuteceSearchUserHome( )
    {
    }

    /**
     * Create an instance of the myLuteceSearchUser class
     * 
     * @param myLuteceSearchUser
     *            The instance of the MyLuteceSearchUser which contains the informations to store
     * @return The instance of myLuteceSearchUser which has been created with its primary key.
     */
    public static MyLuteceSearchUser create( MyLuteceSearchUser myLuteceSearchUser )
    {
        _dao.insert( myLuteceSearchUser, _plugin );
        String strIdMyLuteceSearchUser = Integer.toString( myLuteceSearchUser.getId( ) );
        IndexationService.addIndexerAction( strIdMyLuteceSearchUser, AppPropertiesService.getProperty( MyLuteceSearchUserIndexer.PROPERTY_INDEXER_NAME ),
                IndexerAction.TASK_CREATE );
        return myLuteceSearchUser;
    }

    /**
     * Update of the myLuteceSearchUser which is specified in parameter
     * 
     * @param myLuteceSearchUser
     *            The instance of the MyLuteceSearchUser which contains the data to store
     * @return The instance of the myLuteceSearchUser which has been updated
     */
    public static MyLuteceSearchUser update( MyLuteceSearchUser myLuteceSearchUser )
    {
        _dao.store( myLuteceSearchUser, _plugin );
        String strIdMyLuteceSearchUser = Integer.toString( myLuteceSearchUser.getId( ) );
        IndexationService.addIndexerAction( strIdMyLuteceSearchUser, AppPropertiesService.getProperty( MyLuteceSearchUserIndexer.PROPERTY_INDEXER_NAME ),
                IndexerAction.TASK_MODIFY );
        return myLuteceSearchUser;
    }

    /**
     * Remove the myLuteceSearchUser whose identifier is specified in parameter
     * 
     * @param nKey
     *            The myLuteceSearchUser Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
        IndexationService.addIndexerAction( String.valueOf( nKey ), AppPropertiesService.getProperty( MyLuteceSearchUserIndexer.PROPERTY_INDEXER_NAME ),
                IndexerAction.TASK_DELETE );
    }

    /**
     * Returns an instance of a myLuteceSearchUser whose identifier is specified in parameter
     * 
     * @param nKey
     *            The myLuteceSearchUser primary key
     * @return an instance of MyLuteceSearchUser
     */
    public static MyLuteceSearchUser findByPrimaryKey( int nKey )
    {
        MyLuteceSearchUser myLuteceSearchUser = _dao.load( nKey, _plugin );
        fillAttributes( myLuteceSearchUser );
        return myLuteceSearchUser;
    }

    /**
     * Returns an instance of a myLuteceSearchUser whose identifier is specified in parameter
     * 
     * @param nKey
     *            The myLuteceSearchUser primary key
     * @return an instance of MyLuteceSearchUser
     */
    public static MyLuteceSearchUser findByConnectId( String strUserName )
    {
        return _dao.loadByConnectId( strUserName, _plugin );
    }

    /**
     * Load the data of all the myLuteceSearchUser without attributes and returns them as a list
     * 
     * @return the list which contains the data of all the myLuteceSearchUser objects
     */
    public static List<MyLuteceSearchUser> getMyLuteceSearchUsersListWithoutAttribute( )
    {

        List<MyLuteceSearchUser> listMyLuteceSearchUser = _dao.selectMyLuteceSearchUsersList( _plugin );
        return listMyLuteceSearchUser;
    }

    /**
     * Load the data of all the myLuteceSearchUser objects and returns them as a list
     * 
     * @return the list which contains the data of all the myLuteceSearchUser objects
     */
    public static List<MyLuteceSearchUser> getMyLuteceSearchUsersList( )
    {

        List<MyLuteceSearchUser> listMyLuteceSearchUser = _dao.selectMyLuteceSearchUsersList( _plugin );
        for ( MyLuteceSearchUser myLuteceSearchUser : listMyLuteceSearchUser )
        {
            fillAttributes( myLuteceSearchUser );
        }

        return listMyLuteceSearchUser;
    }

    /**
     * Load the id of all the myLuteceSearchUser objects and returns them as a list
     * 
     * @return the list which contains the id of all the myLuteceSearchUser objects
     */
    public static List<Integer> getIdMyLuteceSearchUsersList( )
    {
        return _dao.selectIdMyLuteceSearchUsersList( _plugin );
    }

    /**
     * Load the data of all the myLuteceSearchUser objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the myLuteceSearchUser objects
     */
    public static ReferenceList getMyLuteceSearchUsersReferenceList( )
    {
        return _dao.selectMyLuteceSearchUsersReferenceList( _plugin );
    }

    /**
     * Fill SearchUser with attributes
     * 
     * @param myLuteceSearchUser
     *            The myLuteceSearchUser
     * @return the referenceList which contains the data of all the myLuteceSearchUser objects
     */
    private static void fillAttributes( MyLuteceSearchUser myLuteceSearchUser )
    {
        List<IAttribute> listMyLuteceAttribute = AttributeHome.findAll( _locale, _myLutecePlugin );
        ReferenceList listMyLuteceSearchUserAttribute = new ReferenceList( );

        for ( IAttribute attribute : listMyLuteceAttribute )
        {
            List<AttributeField> listAttributeFields = AttributeFieldHome.selectAttributeFieldsByIdAttribute( attribute.getIdAttribute( ), _myLutecePlugin );
            attribute.setListAttributeFields( listAttributeFields );
        }

        for ( IAttribute attribute : listMyLuteceAttribute )
        {
            List<MyLuteceUserField> listUserFields = MyLuteceUserFieldHome.selectUserFieldsByIdUserIdAttribute( myLuteceSearchUser.getId( ), attribute.getIdAttribute( ),
                    _myLutecePlugin );
            for ( AttributeField attributeField : attribute.getListAttributeFields( ) )
            {
                MyLuteceUserField myLuteceUserField = listUserFields.stream( ).limit( 1 )
                        .filter( userField -> userField.getAttribute( ).getIdAttribute( ) == attributeField.getAttribute( ).getIdAttribute( ) ).findAny( )
                        .orElse( null );
                if ( myLuteceUserField != null )
                {
                    ReferenceItem myLuteceSearchUserAttribute = new ReferenceItem( );
                    myLuteceSearchUserAttribute.setCode( myLuteceUserField.getValue( ) );
                    myLuteceSearchUserAttribute.setName( String.valueOf( attribute.getIdAttribute( ) ) );
                    listMyLuteceSearchUserAttribute.add( myLuteceSearchUserAttribute );
                }
            }
        }

        myLuteceSearchUser.setAttributes( listMyLuteceSearchUserAttribute );
    }

}
