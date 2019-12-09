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
 * This class provides Data Access methods for LocalUser objects
 */
public final class LocalUserDAO implements ILocalUserDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_local_user, login, given_name, family_name, email FROM mylutece_users_localuser WHERE id_local_user = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO mylutece_users_localuser ( login, given_name, family_name, email ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM mylutece_users_localuser WHERE id_local_user = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE mylutece_users_localuser SET id_local_user = ?, login = ?, given_name = ?, family_name = ?, email = ? WHERE id_local_user = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_local_user, login, given_name, family_name, email FROM mylutece_users_localuser";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_local_user FROM mylutece_users_localuser";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( LocalUser localUser, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, localUser.getLogin( ) );
            daoUtil.setString( nIndex++, localUser.getGivenName( ) );
            daoUtil.setString( nIndex++, localUser.getFamilyName( ) );
            daoUtil.setString( nIndex++, localUser.getEmail( ) );
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                localUser.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public LocalUser load( int nKey, Plugin plugin )
    {
        LocalUser localUser = null;
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                localUser = new LocalUser( );
                int nIndex = 1;
                localUser.setId( daoUtil.getInt( nIndex++ ) );
                localUser.setLogin( daoUtil.getString( nIndex++ ) );
                localUser.setGivenName( daoUtil.getString( nIndex++ ) );
                localUser.setFamilyName( daoUtil.getString( nIndex++ ) );
                localUser.setEmail( daoUtil.getString( nIndex++ ) );
            }
        }
        return localUser;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( LocalUser localUser, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, localUser.getId( ) );
            daoUtil.setString( nIndex++, localUser.getLogin( ) );
            daoUtil.setString( nIndex++, localUser.getGivenName( ) );
            daoUtil.setString( nIndex++, localUser.getFamilyName( ) );
            daoUtil.setString( nIndex++, localUser.getEmail( ) );
            daoUtil.setInt( nIndex, localUser.getId( ) );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<LocalUser> selectLocalUsersList( Plugin plugin )
    {
        List<LocalUser> localUserList = new ArrayList<LocalUser>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                LocalUser localUser = new LocalUser( );
                int nIndex = 1;
                localUser.setId( daoUtil.getInt( nIndex++ ) );
                localUser.setLogin( daoUtil.getString( nIndex++ ) );
                localUser.setGivenName( daoUtil.getString( nIndex++ ) );
                localUser.setFamilyName( daoUtil.getString( nIndex++ ) );
                localUser.setEmail( daoUtil.getString( nIndex++ ) );
                localUserList.add( localUser );
            }
        }
        return localUserList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdLocalUsersList( Plugin plugin )
    {
        List<Integer> localUserList = new ArrayList<Integer>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                localUserList.add( daoUtil.getInt( 1 ) );
            }
        }
        return localUserList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectLocalUsersReferenceList( Plugin plugin )
    {
        ReferenceList localUserList = new ReferenceList( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                localUserList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
            }
        }
        return localUserList;
    }
}
