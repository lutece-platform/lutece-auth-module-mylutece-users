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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.search.IndexationService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.plugins.mylutece.modules.users.service.search.LocalUserIndexer;
import fr.paris.lutece.plugins.mylutece.modules.users.utils.LocalUserIndexerUtils;
import fr.paris.lutece.portal.business.indexeraction.IndexerAction;

import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for LocalUser objects
 */
public final class LocalUserHome
{
    // Static variable pointed at the DAO instance
    private static ILocalUserDAO _dao = SpringContextService.getBean( "mylutece-users.localUserDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "mylutece-users" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private LocalUserHome( )
    {
    }

    /**
     * Create an instance of the localUser class
     * 
     * @param localUser
     *            The instance of the LocalUser which contains the informations to store
     * @return The instance of localUser which has been created with its primary key.
     */
    public static LocalUser create( LocalUser localUser )
    {
        _dao.insert( localUser, _plugin );

        String strIdLocalUser = Integer.toString( localUser.getId( ) );
        IndexationService.addIndexerAction( strIdLocalUser, AppPropertiesService.getProperty( LocalUserIndexer.PROPERTY_INDEXER_NAME ),
                IndexerAction.TASK_CREATE );

        LocalUserIndexerUtils.addIndexerAction( strIdLocalUser, IndexerAction.TASK_CREATE );

        return localUser;
    }

    /**
     * Update of the localUser which is specified in parameter
     * 
     * @param localUser
     *            The instance of the LocalUser which contains the data to store
     * @return The instance of the localUser which has been updated
     */
    public static LocalUser update( LocalUser localUser )
    {
        _dao.store( localUser, _plugin );

        return localUser;
    }

    /**
     * Remove the localUser whose identifier is specified in parameter
     * 
     * @param nKey
     *            The localUser Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a localUser whose identifier is specified in parameter
     * 
     * @param nKey
     *            The localUser primary key
     * @return an instance of LocalUser
     */
    public static LocalUser findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Returns an instance of a localUser whose identifier is specified in parameter
     * 
     * @param nKey
     *            The localUser primary key
     * @return an instance of LocalUser
     */
    public static LocalUser findByConnectId( String strUserName )
    {
        return _dao.loadByConnectId( strUserName, _plugin );
    }

    /**
     * Load the data of all the localUser objects and returns them as a list
     * 
     * @return the list which contains the data of all the localUser objects
     */
    public static List<LocalUser> getLocalUsersList( )
    {
        return _dao.selectLocalUsersList( _plugin );
    }

    /**
     * Load the id of all the localUser objects and returns them as a list
     * 
     * @return the list which contains the id of all the localUser objects
     */
    public static List<Integer> getIdLocalUsersList( )
    {
        return _dao.selectIdLocalUsersList( _plugin );
    }

    /**
     * Load the data of all the localUser objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the localUser objects
     */
    public static ReferenceList getLocalUsersReferenceList( )
    {
        return _dao.selectLocalUsersReferenceList( _plugin );
    }
}
