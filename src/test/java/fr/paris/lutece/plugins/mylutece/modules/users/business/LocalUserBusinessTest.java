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

import fr.paris.lutece.test.LuteceTestCase;

/**
 * This is the business class test for the object LocalUser
 */
public class LocalUserBusinessTest extends LuteceTestCase
{
    private static final String LOGIN1 = "Login1";
    private static final String LOGIN2 = "Login2";
    private static final String GIVENNAME1 = "GivenName1";
    private static final String GIVENNAME2 = "GivenName2";
    private static final String LASTNAME1 = "LastName1";
    private static final String LASTNAME2 = "LastName2";
    private static final String EMAIL1 = "Email1";
    private static final String EMAIL2 = "Email2";

    /**
     * test LocalUser
     */
    public void testBusiness( )
    {
        // Initialize an object
        LocalUser localUser = new LocalUser( );
        localUser.setLogin( LOGIN1 );
        localUser.setGivenName( GIVENNAME1 );
        localUser.setLastName( LASTNAME1 );
        localUser.setEmail( EMAIL1 );

        // Create test
        LocalUserHome.create( localUser );
        LocalUser localUserStored = LocalUserHome.findByPrimaryKey( localUser.getId( ) );
        assertEquals( localUserStored.getLogin( ), localUser.getLogin( ) );
        assertEquals( localUserStored.getGivenName( ), localUser.getGivenName( ) );
        assertEquals( localUserStored.getLastName( ), localUser.getLastName( ) );
        assertEquals( localUserStored.getEmail( ), localUser.getEmail( ) );

        // Update test
        localUser.setLogin( LOGIN2 );
        localUser.setGivenName( GIVENNAME2 );
        localUser.setLastName( LASTNAME2 );
        localUser.setEmail( EMAIL2 );
        LocalUserHome.update( localUser );
        localUserStored = LocalUserHome.findByPrimaryKey( localUser.getId( ) );
        assertEquals( localUserStored.getLogin( ), localUser.getLogin( ) );
        assertEquals( localUserStored.getGivenName( ), localUser.getGivenName( ) );
        assertEquals( localUserStored.getLastName( ), localUser.getLastName( ) );
        assertEquals( localUserStored.getEmail( ), localUser.getEmail( ) );

        // List test
        LocalUserHome.getLocalUsersList( );

        // Delete test
        LocalUserHome.remove( localUser.getId( ) );
        localUserStored = LocalUserHome.findByPrimaryKey( localUser.getId( ) );
        assertNull( localUserStored );

    }

}
