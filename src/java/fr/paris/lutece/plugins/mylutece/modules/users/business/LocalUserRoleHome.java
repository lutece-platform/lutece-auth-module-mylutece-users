/*
 * Copyright (c) 2002-2019, Mairie de Paris
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
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;
import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for LocalUserRole objects
 */
public final class LocalUserRoleHome
{
    // Static variable pointed at the DAO instance
    private static ILocalUserRoleDAO _dao = SpringContextService.getBean( "mylutece-users.localUserRoleDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "mylutece-users" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private LocalUserRoleHome( )
    {
    }

    /**
     * Create an instance of the localUserRole class
     * 
     * @param localUserRole
     *            The instance of the LocalUserRole which contains the informations to store
     * @return The instance of localUserRole which has been created with its primary key.
     */
    public static LocalUserRole create( LocalUserRole localUserRole )
    {
        _dao.insert( localUserRole, _plugin );
        return localUserRole;
    }

    /**
     * Update of the localUserRole which is specified in parameter
     * 
     * @param localUserRole
     *            The instance of the LocalUserRole which contains the data to store
     * @return The instance of the localUserRole which has been updated
     */
    public static LocalUserRole update( LocalUserRole localUserRole )
    {
        _dao.store( localUserRole, _plugin );
        return localUserRole;
    }

    /**
     * Remove the localUserRole whose identifier is specified in parameter
     * 
     * @param nKey
     *            The localUserRole Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a localUserRole whose identifier is specified in parameter
     * 
     * @param nKey
     *            The localUserRole primary key
     * @return an instance of LocalUserRole
     */
    public static LocalUserRole findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the localUserRole objects and returns them as a list
     * 
     * @return the list which contains the data of all the localUserRole objects
     */
    public static List<LocalUserRole> getLocalUserRolesList( )
    {
        return _dao.selectLocalUserRolesList( _plugin );
    }

    /**
     * Load the id of all the localUserRole objects and returns them as a list
     * 
     * @return the list which contains the id of all the localUserRole objects
     */
    public static List<Integer> getIdLocalUserRolesList( )
    {
        return _dao.selectIdLocalUserRolesList( _plugin );
    }

    /**
     * Load the data of all the localUserRole objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the localUserRole objects
     */
    public static ReferenceList getLocalUserRolesReferenceList( )
    {
        return _dao.selectLocalUserRolesReferenceList( _plugin );
    }

    /**
     * Load the data of all the localUserRole objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the localUserRole objects
     */
    public static List<LocalUserRole> getLocalUserRolesListByUserId( int nIdLocalUser )
    {
        return _dao.selectLocalUserRolesListByUserId( nIdLocalUser, _plugin );
    }

    /**
     * Remove each localUserRole whose identifier is specified in the list
     * 
     * @param localUserRoleToDeleteList
     *            The LocalUserRole List to delete
     */
    public static void removeLocalUserRoles( List<LocalUserRole> localUserRoleToDeleteList )
    {
        for ( LocalUserRole localUserRole : localUserRoleToDeleteList )
        {
            LocalUserRoleHome.remove( localUserRole.getId( ) );
        }
    }

    /**
     * Create each localUserRole whose identifier is specified in the list
     * 
     * @param myLuteceRoleKeyToAssignList
     *            The MyLutece role keys List to assign
     * @param nIdLocalUser
     *            The localUser primary key
     */
    public static void createLocalUserRoles( List<String> myLuteceRoleKeyToAssignList, int nIdLocalUser )
    {
        for ( String myLuteceRoleKey : myLuteceRoleKeyToAssignList )
        {
            LocalUserRole localUserRole = new LocalUserRole( );
            localUserRole.setIdLocaluser( nIdLocalUser );
            localUserRole.setRoleKey( myLuteceRoleKey );
            LocalUserRoleHome.create( localUserRole );
        }
    }
}
