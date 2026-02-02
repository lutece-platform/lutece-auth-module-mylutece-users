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
import fr.paris.lutece.util.ReferenceList;
import jakarta.enterprise.inject.spi.CDI;
import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for MyLuteceUserRole objects
 */
public class MyLuteceUserRoleHome
{
    // Static variable pointed at the DAO instance
    private static IMyLuteceUserRoleDAO _dao = CDI.current( ).select( IMyLuteceUserRoleDAO.class ).get( );
    private static Plugin _plugin = PluginService.getPlugin( "mylutece-users" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private MyLuteceUserRoleHome( )
    {
    }

    /**
     * Create an instance of the myLuteceUserRole class
     * 
     * @param myLuteceUserRole
     *            The instance of the MyLuteceUserRole which contains the informations to store
     * @return The instance of myLuteceUserRole which has been created with its primary key.
     */
    public static MyLuteceUserRole create( MyLuteceUserRole myLuteceUserRole )
    {
        _dao.insert( myLuteceUserRole, _plugin );
        return myLuteceUserRole;
    }

    /**
     * Update of the myLuteceUserRole which is specified in parameter
     * 
     * @param myLuteceUserRole
     *            The instance of the MyLuteceUserRole which contains the data to store
     * @return The instance of the myLuteceUserRole which has been updated
     */
    public static MyLuteceUserRole update( MyLuteceUserRole myLuteceUserRole )
    {
        _dao.store( myLuteceUserRole, _plugin );
        return myLuteceUserRole;
    }

    /**
     * Remove the myLuteceUserRole whose identifier is specified in parameter
     * 
     * @param nKey
     *            The myLuteceUserRole Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a myLuteceUserRole whose identifier is specified in parameter
     * 
     * @param nKey
     *            The myLuteceUserRole primary key
     * @return an instance of MyLuteceUserRole
     */
    public static MyLuteceUserRole findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the myLuteceUserRole objects and returns them as a list
     * 
     * @return the list which contains the data of all the myLuteceUserRole objects
     */
    public static List<MyLuteceUserRole> getMyLuteceUserRolesList( )
    {
        return _dao.selectMyLuteceUserRolesList( _plugin );
    }

    /**
     * Load the id of all the myLuteceUserRole objects and returns them as a list
     * 
     * @return the list which contains the id of all the myLuteceUserRole objects
     */
    public static List<Integer> getIdMyLuteceUserRolesList( )
    {
        return _dao.selectIdMyLuteceUserRolesList( _plugin );
    }

    /**
     * Load the data of all the myLuteceUserRole objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the myLuteceUserRole objects
     */
    public static ReferenceList getMyLuteceUserRolesReferenceList( )
    {
        return _dao.selectMyLuteceUserRolesReferenceList( _plugin );
    }

    /**
     * Load the data of all the myLuteceUserRole objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the myLuteceUserRole objects
     */
    public static List<MyLuteceUserRole> getMyLuteceUserRolesListByUserId( int nIdSearchUser )
    {
        return _dao.selectMyLuteceUserRolesListByUserId( nIdSearchUser, _plugin );
    }

    /**
     * Remove each myLuteceUserRole whose identifier is specified in the list
     * 
     * @param myLuteceUserRoleToDeleteList
     *            The MyLuteceUserRole List to delete
     */
    public static void removeMyLuteceUserRoles( List<MyLuteceUserRole> myLuteceUserRoleToDeleteList )
    {
        for ( MyLuteceUserRole myLuteceUserRole : myLuteceUserRoleToDeleteList )
        {
            MyLuteceUserRoleHome.remove( myLuteceUserRole.getId( ) );
        }
    }

    /**
     * Create each myLuteceUserRole whose identifier is specified in the list
     * 
     * @param myLuteceRoleKeyToAssignList
     *            The MyLutece role keys List to assign
     * @param nIdSearchUser
     *            The searchUser primary key
     */
    public static void createMyLuteceUserRoles( List<String> myLuteceRoleKeyToAssignList, int nIdSearchUser )
    {
        for ( String myLuteceRoleKey : myLuteceRoleKeyToAssignList )
        {
            MyLuteceUserRole myLuteceUserRole = new MyLuteceUserRole( );
            myLuteceUserRole.setIdMyLuteceSearchUser( nIdSearchUser );
            myLuteceUserRole.setRoleKey( myLuteceRoleKey );
            MyLuteceUserRoleHome.create( myLuteceUserRole );
        }
    }
}
