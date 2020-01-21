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
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for LocalUserRole objects
 */
public final class LocalUserRoleDAO implements ILocalUserRoleDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_role, id_localuser, role_key FROM mylutece_users_luteceuser_role WHERE id_role = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO mylutece_users_luteceuser_role ( id_localuser, role_key ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM mylutece_users_luteceuser_role WHERE id_role = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE mylutece_users_luteceuser_role SET id_role = ?, id_localuser = ?, role_key = ? WHERE id_role = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_role, id_localuser, role_key FROM mylutece_users_luteceuser_role";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_role FROM mylutece_users_luteceuser_role";
    private static final String SQL_QUERY_SELECTALL_BY_LOCALUSER_ID = "SELECT id_role, id_localuser, role_key FROM mylutece_users_luteceuser_role WHERE id_localuser = ?";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( LocalUserRole localUserRole, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, localUserRole.getIdLocaluser( ) );
            daoUtil.setString( nIndex++, localUserRole.getRoleKey( ) );
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                localUserRole.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public LocalUserRole load( int nKey, Plugin plugin )
    {
        LocalUserRole localUserRole = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                localUserRole = new LocalUserRole( );
                int nIndex = 1;
                localUserRole.setId( daoUtil.getInt( nIndex++ ) );
                localUserRole.setIdLocaluser( daoUtil.getInt( nIndex++ ) );
                localUserRole.setRoleKey( daoUtil.getString( nIndex++ ) );
            }
        }
        return localUserRole;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( LocalUserRole localUserRole, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, localUserRole.getId( ) );
            daoUtil.setInt( nIndex++, localUserRole.getIdLocaluser( ) );
            daoUtil.setString( nIndex++, localUserRole.getRoleKey( ) );
            daoUtil.setInt( nIndex, localUserRole.getId( ) );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<LocalUserRole> selectLocalUserRolesList( Plugin plugin )
    {
        List<LocalUserRole> localUserRoleList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                LocalUserRole localUserRole = new LocalUserRole( );
                int nIndex = 1;
                localUserRole.setId( daoUtil.getInt( nIndex++ ) );
                localUserRole.setIdLocaluser( daoUtil.getInt( nIndex++ ) );
                localUserRole.setRoleKey( daoUtil.getString( nIndex++ ) );
                localUserRoleList.add( localUserRole );
            }
        }
        return localUserRoleList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdLocalUserRolesList( Plugin plugin )
    {
        List<Integer> localUserRoleList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                localUserRoleList.add( daoUtil.getInt( 1 ) );
            }
        }
        return localUserRoleList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectLocalUserRolesReferenceList( Plugin plugin )
    {
        ReferenceList localUserRoleList = new ReferenceList( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                localUserRoleList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
            }
        }
        return localUserRoleList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<LocalUserRole> selectLocalUserRolesListByUserId( int nIdLocalUser, Plugin plugin )
    {
        List<LocalUserRole> localUserRoleList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_BY_LOCALUSER_ID, plugin ) )
        {
            daoUtil.setInt( 1, nIdLocalUser );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                LocalUserRole localUserRole = new LocalUserRole( );
                int nIndex = 1;
                localUserRole.setId( daoUtil.getInt( nIndex++ ) );
                localUserRole.setIdLocaluser( daoUtil.getInt( nIndex++ ) );
                localUserRole.setRoleKey( daoUtil.getString( nIndex++ ) );
                localUserRoleList.add( localUserRole );
            }
        }
        return localUserRoleList;
    }
}
