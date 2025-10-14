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
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for MyLuteceUserRole objects
 */
public final class MyLuteceUserRoleDAO implements IMyLuteceUserRoleDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_role, id_searchuser, role_key FROM mylutece_users_userrole WHERE id_role = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO mylutece_users_userrole ( id_searchuser, role_key ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM mylutece_users_userrole WHERE id_role = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE mylutece_users_userrole SET id_role = ?, id_searchuser = ?, role_key = ? WHERE id_role = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_role, id_searchuser, role_key FROM mylutece_users_userrole";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_role FROM mylutece_users_userrole";
    private static final String SQL_QUERY_SELECTALL_BY_SEARCHUSER_ID = "SELECT id_role, id_searchuser, role_key FROM mylutece_users_userrole WHERE id_searchuser = ?";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( MyLuteceUserRole myLuteceUserRole, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, myLuteceUserRole.getIdMyLuteceSearchUser( ) );
            daoUtil.setString( nIndex++, myLuteceUserRole.getRoleKey( ) );
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                myLuteceUserRole.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public MyLuteceUserRole load( int nKey, Plugin plugin )
    {
        MyLuteceUserRole myLuteceUserRole = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                myLuteceUserRole = new MyLuteceUserRole( );
                int nIndex = 1;
                myLuteceUserRole.setId( daoUtil.getInt( nIndex++ ) );
                myLuteceUserRole.setIdMyLuteceSearchUser( daoUtil.getInt( nIndex++ ) );
                myLuteceUserRole.setRoleKey( daoUtil.getString( nIndex++ ) );
            }
        }
        return myLuteceUserRole;
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
    public void store( MyLuteceUserRole myLuteceUserRole, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, myLuteceUserRole.getId( ) );
            daoUtil.setInt( nIndex++, myLuteceUserRole.getIdMyLuteceSearchUser( ) );
            daoUtil.setString( nIndex++, myLuteceUserRole.getRoleKey( ) );
            daoUtil.setInt( nIndex, myLuteceUserRole.getId( ) );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<MyLuteceUserRole> selectMyLuteceUserRolesList( Plugin plugin )
    {
        List<MyLuteceUserRole> myLuteceUserRoleList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                MyLuteceUserRole myLuteceUserRole = new MyLuteceUserRole( );
                int nIndex = 1;
                myLuteceUserRole.setId( daoUtil.getInt( nIndex++ ) );
                myLuteceUserRole.setIdMyLuteceSearchUser( daoUtil.getInt( nIndex++ ) );
                myLuteceUserRole.setRoleKey( daoUtil.getString( nIndex++ ) );
                myLuteceUserRoleList.add( myLuteceUserRole );
            }
        }
        return myLuteceUserRoleList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdMyLuteceUserRolesList( Plugin plugin )
    {
        List<Integer> myLuteceUserRoleList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                myLuteceUserRoleList.add( daoUtil.getInt( 1 ) );
            }
        }
        return myLuteceUserRoleList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectMyLuteceUserRolesReferenceList( Plugin plugin )
    {
        ReferenceList myLuteceUserRoleList = new ReferenceList( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                myLuteceUserRoleList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
            }
        }
        return myLuteceUserRoleList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<MyLuteceUserRole> selectMyLuteceUserRolesListByUserId( int nIdSearchUser, Plugin plugin )
    {
        List<MyLuteceUserRole> myLuteceUserRoleList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_BY_SEARCHUSER_ID, plugin ) )
        {
            daoUtil.setInt( 1, nIdSearchUser );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                MyLuteceUserRole myLuteceUserRole = new MyLuteceUserRole( );
                int nIndex = 1;
                myLuteceUserRole.setId( daoUtil.getInt( nIndex++ ) );
                myLuteceUserRole.setIdMyLuteceSearchUser( daoUtil.getInt( nIndex++ ) );
                myLuteceUserRole.setRoleKey( daoUtil.getString( nIndex++ ) );
                myLuteceUserRoleList.add( myLuteceUserRole );
            }
        }
        return myLuteceUserRoleList;
    }
}
