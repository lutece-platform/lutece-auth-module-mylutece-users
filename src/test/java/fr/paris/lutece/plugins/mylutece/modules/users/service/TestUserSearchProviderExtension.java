/*
 * Copyright (c) 2002-2026, City of Paris
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
import fr.paris.lutece.util.ReferenceList;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;

/**
 * Registers, for the tests, the user search provider the module uses by default (mylutece.myLuteceUserSearchProvider):
 * the module ships none, the sites declare theirs. It finds two users.
 */
public class TestUserSearchProviderExtension implements Extension
{
    public static final String PROVIDER_NAME = "mylutece.myLuteceUserSearchProvider";

    /**
     * A provider that finds two users whatever the criteria
     */
    public static class TestUserSearchProvider implements IUserSearchProvider
    {
        private static MyLuteceSearchUser newUser( String strLogin )
        {
            MyLuteceSearchUser user = new MyLuteceSearchUser( );
            user.setLogin( strLogin );
            user.setLastName( "Last " + strLogin );
            user.setGivenName( "Given " + strLogin );
            user.setEmail( strLogin + "@example.org" );
            user.setProviderUserId( "provider-" + strLogin );
            return user;
        }

        @Override
        public List<MyLuteceSearchUser> findUsers( String strLastName, String strGivenName, String strEmail, ReferenceList listProviderAttribute )
        {
            return List.of( newUser( "first" ), newUser( "second" ) );
        }

        @Override
        public List<String> getAllAttributes( )
        {
            return List.of( );
        }

        @Override
        public MyLuteceSearchUser getUserById( String strUserId )
        {
            return null;
        }

        @Override
        public List<MyLuteceSearchUser> getUsersByIds( List<String> listUserIds )
        {
            return List.of( );
        }
    }

    /**
     * Adds the provider as a named bean
     *
     * @param abd
     *            the event
     */
    protected void addProvider( @Observes final AfterBeanDiscovery abd )
    {
        abd.addBean( ).beanClass( TestUserSearchProvider.class ).name( PROVIDER_NAME ).addTypes( TestUserSearchProvider.class, IUserSearchProvider.class )
                .addQualifier( NamedLiteral.of( PROVIDER_NAME ) ).scope( ApplicationScoped.class ).produceWith( obj -> new TestUserSearchProvider( ) );
    }
}
