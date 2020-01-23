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
package fr.paris.lutece.plugins.mylutece.modules.users.service;

import java.util.List;
import java.util.stream.Collectors;

import fr.paris.lutece.plugins.mylutece.modules.users.business.LocalUserRole;
import fr.paris.lutece.plugins.mylutece.modules.users.business.LocalUserRoleHome;

/**
 * Service class for LocalUserRole
 *
 */
public final class LocalUserRoleService
{
    public static final String BEAN_NAME = "mylutece-users.localUserRoleService";

    private LocalUserRoleService( )
    {
    }

    /**
     * Process to user assignements from Mylutece Role List
     * 
     * @param myLuteceRoleKeysList
     *            The MyLutece role keys List
     * @param nIdLocalUser
     *            The localUser id
     */
    public static void doLocalUserAssignements( List<String> myLuteceRoleKeysList, int nIdLocalUser )
    {

        List<LocalUserRole> localUserRolesList = LocalUserRoleHome.getLocalUserRolesListByUserId( nIdLocalUser );
        if ( myLuteceRoleKeysList == null )
        {
            LocalUserRoleHome.removeLocalUserRoles( localUserRolesList );
            return;
        }
        // find all localUser roles to delete.
        List<LocalUserRole> localUsersRolesToDeleteList = localUserRolesList.stream( )
                .filter( currentLocalUserRoleKey -> !myLuteceRoleKeysList.contains( currentLocalUserRoleKey.getRoleKey( ) ) ).collect( Collectors.toList( ) );
        // find all myLutece roles to assign.
        List<String> myLuteceRoleKeyToAssignList = myLuteceRoleKeysList.stream( )
                .filter( myLuteceRoleKey -> localUserRolesList.stream( ).noneMatch( localUserRole -> myLuteceRoleKey.equals( localUserRole.getRoleKey( ) ) ) )
                .collect( Collectors.toList( ) );

        if ( localUsersRolesToDeleteList != null )
        {
            LocalUserRoleHome.removeLocalUserRoles( localUsersRolesToDeleteList );
        }
        if ( myLuteceRoleKeyToAssignList != null )
        {
            LocalUserRoleHome.createLocalUserRoles( myLuteceRoleKeyToAssignList, nIdLocalUser );
        }
    }

}
