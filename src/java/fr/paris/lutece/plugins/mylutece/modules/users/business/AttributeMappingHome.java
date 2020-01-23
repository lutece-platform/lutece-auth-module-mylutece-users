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
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for AttributeMapping objects
 */
public final class AttributeMappingHome
{
    // Static variable pointed at the DAO instance
    private static IAttributeMappingDAO _dao = SpringContextService.getBean( "mylutece_users.attributeMappingDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "mylutece_users" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private AttributeMappingHome( )
    {
    }

    /**
     * Create an instance of the attributeMapping class
     * 
     * @param attributeMapping
     *            The instance of the AttributeMapping which contains the informations to store
     * @return The instance of attributeMapping which has been created with its primary key.
     */
    public static AttributeMapping create( AttributeMapping attributeMapping )
    {
        _dao.insert( attributeMapping, _plugin );

        return attributeMapping;
    }

    /**
     * Update of the attributeMapping which is specified in parameter
     * 
     * @param attributeMapping
     *            The instance of the AttributeMapping which contains the data to store
     * @return The instance of the attributeMapping which has been updated
     */
    public static AttributeMapping update( AttributeMapping attributeMapping )
    {
        _dao.store( attributeMapping, _plugin );

        return attributeMapping;
    }

    /**
     * Remove the attributeMapping whose identifier is specified in parameter
     * 
     * @param nKey
     *            The attributeMapping Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a attributeMapping whose identifier is specified in parameter
     * 
     * @param nKey
     *            The attributeMapping primary key
     * @return an instance of AttributeMapping
     */
    public static AttributeMapping findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the attributeMapping objects and returns them as a list
     * 
     * @return the list which contains the data of all the attributeMapping objects
     */
    public static List<AttributeMapping> getAttributeMappingsList( )
    {
        return _dao.selectAttributeMappingsList( _plugin );
    }

    /**
     * Load the id of all the attributeMapping objects and returns them as a list
     * 
     * @return the list which contains the id of all the attributeMapping objects
     */
    public static List<Integer> getIdAttributeMappingsList( )
    {
        return _dao.selectIdAttributeMappingsList( _plugin );
    }

    /**
     * Load the data of all the attributeMapping objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the attributeMapping objects
     */
    public static ReferenceList getAttributeMappingsReferenceList( )
    {
        return _dao.selectAttributeMappingsReferenceList( _plugin );
    }
}
