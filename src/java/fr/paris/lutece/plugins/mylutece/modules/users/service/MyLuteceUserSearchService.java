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
import fr.paris.lutece.portal.service.util.CdiHelper;
import fr.paris.lutece.util.ReferenceList;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * User Search Service.
 * Delegates to a configured IUserSearchProvider implementation resolved by bean name.
 * The bean name is configurable via the property {@code mylutece.users.searchProviderName}.
 */
@ApplicationScoped
public class MyLuteceUserSearchService implements IUserSearchProvider
{
    private static final String DEFAULT_PROVIDER = "mylutece.myLuteceUserSearchProvider";

    @Inject
    @ConfigProperty( name = "mylutece.users.searchProviderName", defaultValue = DEFAULT_PROVIDER )
    private String _strProviderName;

    @Inject
    private Instance<IUserSearchProvider> _providers;

    /**
     * Returns the configured IUserSearchProvider instance.
     *
     * @return the resolved IUserSearchProvider
     */
    public IUserSearchProvider getProvider( )
    {
        return CdiHelper.resolve( _providers, _strProviderName );
    }

    /**
     * Returns the instance of the singleton.
     *
     * @return The instance of the singleton
     */
    public static IUserSearchProvider getInstance( )
    {
        return CdiHelper.getBean( MyLuteceUserSearchService.class ).getProvider( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MyLuteceSearchUser> findUsers( String strParameterLastName, String strParameterGivenName, String strParameterCriteriaMail,
            ReferenceList listProviderAttribute )
    {
        return getInstance( ).findUsers( strParameterLastName, strParameterGivenName, strParameterCriteriaMail, listProviderAttribute );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllAttributes( )
    {
        return getInstance( ).getAllAttributes( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MyLuteceSearchUser getUserById( String strUserId )
    {
        return getInstance( ).getUserById( strUserId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MyLuteceSearchUser> getUsersByIds( List<String> userIds )
    {
        return getInstance( ).getUsersByIds( userIds );
    }
}
