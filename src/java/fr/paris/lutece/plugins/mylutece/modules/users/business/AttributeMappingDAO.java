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
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for AttributeMapping objects
 */
@ApplicationScoped
public class AttributeMappingDAO implements IAttributeMappingDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_mylutece_attribute, id_provider_attribute FROM mylutece_users_attribute_mapping WHERE id_mylutece_attribute = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO mylutece_users_attribute_mapping ( id_mylutece_attribute, id_provider_attribute ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM mylutece_users_attribute_mapping WHERE id_mylutece_attribute = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE mylutece_users_attribute_mapping SET id_mylutece_attribute = ?, id_provider_attribute = ? WHERE id_mylutece_attribute = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_mylutece_attribute, id_provider_attribute FROM mylutece_users_attribute_mapping";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_mylutece_attribute FROM mylutece_users_attribute_mapping";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( AttributeMapping attributeMapping, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, attributeMapping.getId( ) );
            daoUtil.setString( nIndex++, attributeMapping.getIdProviderAttribute( ) );
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                attributeMapping.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public AttributeMapping load( int nKey, Plugin plugin )
    {
        AttributeMapping attributeMapping = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                attributeMapping = new AttributeMapping( );
                int nIndex = 1;
                attributeMapping.setId( daoUtil.getInt( nIndex++ ) );
                attributeMapping.setIdProviderAttribute( daoUtil.getString( nIndex++ ) );
            }
        }
        return attributeMapping;
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
    public void store( AttributeMapping attributeMapping, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, attributeMapping.getId( ) );
            daoUtil.setString( nIndex++, attributeMapping.getIdProviderAttribute( ) );
            daoUtil.setInt( nIndex, attributeMapping.getId( ) );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<AttributeMapping> selectAttributeMappingsList( Plugin plugin )
    {
        List<AttributeMapping> attributeMappingList = new ArrayList<AttributeMapping>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                AttributeMapping attributeMapping = new AttributeMapping( );
                int nIndex = 1;
                attributeMapping.setId( daoUtil.getInt( nIndex++ ) );
                attributeMapping.setIdProviderAttribute( daoUtil.getString( nIndex++ ) );
                attributeMappingList.add( attributeMapping );
            }
        }
        return attributeMappingList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdAttributeMappingsList( Plugin plugin )
    {
        List<Integer> attributeMappingList = new ArrayList<Integer>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                attributeMappingList.add( daoUtil.getInt( 1 ) );
            }
        }
        return attributeMappingList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectAttributeMappingsReferenceList( Plugin plugin )
    {
        ReferenceList attributeMappingList = new ReferenceList( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                attributeMappingList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
            }
        }
        return attributeMappingList;
    }
}
