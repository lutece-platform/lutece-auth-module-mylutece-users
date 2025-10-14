/*
 * Copyright (c) 2002-2025, City of Paris
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

import fr.paris.lutece.plugins.mylutece.service.search.IUserSearchProvider;
import fr.paris.lutece.plugins.mylutece.service.search.MyLuteceSearchUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

/**
 * User Search Service
 * Singleton service that delegates to a configured IUserSearchProvider implementation
 */
public final class MyLuteceUserSearchService implements IUserSearchProvider
{
    private static final String BEAN_NAME = "mylutece.myLuteceUserSearchProvider";
    private static IUserSearchProvider _userSearchProvider;

    /**
     * Private constructor
     */
    private MyLuteceUserSearchService( )
    {
    }

    /**
     * Initialize the service
     */
    public void init( )
    {
        _userSearchProvider = SpringContextService.getBean( BEAN_NAME );
    }

    /**
     * Returns the instance of the singleton
     *
     * @return The instance of the singleton
     */
    public static IUserSearchProvider getInstance( )
    {
        if ( _userSearchProvider == null )
        {
            _userSearchProvider = SpringContextService.getBean( BEAN_NAME );
        }
        return _userSearchProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MyLuteceSearchUser> findUsers( String strParameterLastName, String strParameterGivenName, String strParameterCriteriaMail,
            ReferenceList listProviderAttribute )
    {
        return _userSearchProvider.findUsers( strParameterLastName, strParameterGivenName, strParameterCriteriaMail, listProviderAttribute );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllAttributes( )
    {
        return _userSearchProvider.getAllAttributes( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MyLuteceSearchUser getUserById( String strUserId )
    {
        return _userSearchProvider.getUserById( strUserId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MyLuteceSearchUser> getUsersByIds( List<String> userIds )
    {
        return _userSearchProvider.getUsersByIds( userIds );
    }
}
