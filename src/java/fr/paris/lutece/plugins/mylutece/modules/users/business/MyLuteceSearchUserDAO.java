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
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for MyLuteceSearchUser objects
 */
public final class MyLuteceSearchUserDAO implements IMyLuteceSearchUserDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT connect_id, login, given_name, last_name, email, connect_id_provider FROM mylutece_users_searchuser WHERE connect_id = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO mylutece_users_searchuser ( login, given_name, last_name, email, connect_id_provider ) VALUES ( ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM mylutece_users_searchuser WHERE connect_id = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE mylutece_users_searchuser SET connect_id = ?, login = ?, given_name = ?, last_name = ?, email = ?, connect_id_provider = ? WHERE connect_id = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT connect_id, login, given_name, last_name, email, connect_id_provider FROM mylutece_users_searchuser";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT connect_id FROM mylutece_users_searchuser";
    private static final String SQL_QUERY_SELECT_BY_CONNECT_ID = "SELECT connect_id, login, given_name, last_name, email, connect_id_provider FROM mylutece_users_searchuser WHERE connect_id_provider = ?";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( MyLuteceSearchUser myLuteceSearchUser, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, myLuteceSearchUser.getLogin( ) );
            daoUtil.setString( nIndex++, myLuteceSearchUser.getGivenName( ) );
            daoUtil.setString( nIndex++, myLuteceSearchUser.getLastName( ) );
            daoUtil.setString( nIndex++, myLuteceSearchUser.getEmail( ) );
            daoUtil.setString( nIndex++, myLuteceSearchUser.getProviderUserId( ) );
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                myLuteceSearchUser.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public MyLuteceSearchUser load( int nKey, Plugin plugin )
    {
        MyLuteceSearchUser myLuteceSearchUser = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                myLuteceSearchUser = new MyLuteceSearchUser( );
                int nIndex = 1;
                myLuteceSearchUser.setId( daoUtil.getInt( nIndex++ ) );
                myLuteceSearchUser.setLogin( daoUtil.getString( nIndex++ ) );
                myLuteceSearchUser.setGivenName( daoUtil.getString( nIndex++ ) );
                myLuteceSearchUser.setLastName( daoUtil.getString( nIndex++ ) );
                myLuteceSearchUser.setEmail( daoUtil.getString( nIndex++ ) );
                myLuteceSearchUser.setProviderUserId( daoUtil.getString( nIndex++ ) );
            }
        }
        return myLuteceSearchUser;
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
    public void store( MyLuteceSearchUser myLuteceSearchUser, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, myLuteceSearchUser.getId( ) );
            daoUtil.setString( nIndex++, myLuteceSearchUser.getLogin( ) );
            daoUtil.setString( nIndex++, myLuteceSearchUser.getGivenName( ) );
            daoUtil.setString( nIndex++, myLuteceSearchUser.getLastName( ) );
            daoUtil.setString( nIndex++, myLuteceSearchUser.getEmail( ) );
            daoUtil.setString( nIndex++, myLuteceSearchUser.getProviderUserId( ) );
            daoUtil.setInt( nIndex, myLuteceSearchUser.getId( ) );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<MyLuteceSearchUser> selectMyLuteceSearchUsersList( Plugin plugin )
    {
        List<MyLuteceSearchUser> myLuteceSearchUserList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                MyLuteceSearchUser myLuteceSearchUser = new MyLuteceSearchUser( );
                int nIndex = 1;
                myLuteceSearchUser.setId( daoUtil.getInt( nIndex++ ) );
                myLuteceSearchUser.setLogin( daoUtil.getString( nIndex++ ) );
                myLuteceSearchUser.setGivenName( daoUtil.getString( nIndex++ ) );
                myLuteceSearchUser.setLastName( daoUtil.getString( nIndex++ ) );
                myLuteceSearchUser.setEmail( daoUtil.getString( nIndex++ ) );
                myLuteceSearchUser.setProviderUserId( daoUtil.getString( nIndex++ ) );
                myLuteceSearchUserList.add( myLuteceSearchUser );
            }
        }
        return myLuteceSearchUserList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdMyLuteceSearchUsersList( Plugin plugin )
    {
        List<Integer> myLuteceSearchUserList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                myLuteceSearchUserList.add( daoUtil.getInt( 1 ) );
            }
        }
        return myLuteceSearchUserList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectMyLuteceSearchUsersReferenceList( Plugin plugin )
    {
        ReferenceList myLuteceSearchUserList = new ReferenceList( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                myLuteceSearchUserList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
            }
        }
        return myLuteceSearchUserList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public MyLuteceSearchUser loadByConnectId( String strUserName, Plugin plugin )
    {

        MyLuteceSearchUser myLuteceSearchUser = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_CONNECT_ID, plugin ) )
        {
            daoUtil.setString( 1, strUserName );
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                myLuteceSearchUser = new MyLuteceSearchUser( );
                int nIndex = 1;
                myLuteceSearchUser.setId( daoUtil.getInt( nIndex++ ) );
                myLuteceSearchUser.setLogin( daoUtil.getString( nIndex++ ) );
                myLuteceSearchUser.setGivenName( daoUtil.getString( nIndex++ ) );
                myLuteceSearchUser.setLastName( daoUtil.getString( nIndex++ ) );
                myLuteceSearchUser.setEmail( daoUtil.getString( nIndex++ ) );
                myLuteceSearchUser.setProviderUserId( daoUtil.getString( nIndex++ ) );
            }
        }
        return myLuteceSearchUser;
    }
}
