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
import fr.paris.lutece.test.LuteceTestCase;

/**
 * This is the business class test for the object MyLuteceSearchUser
 */
public class MyLuteceSearchUserBusinessTest extends LuteceTestCase
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
     * test MyLuteceSearchUser
     */
    public void testBusiness( )
    {
        // Initialize an object
        MyLuteceSearchUser myLuteceSearchUser = new MyLuteceSearchUser( );
        myLuteceSearchUser.setLogin( LOGIN1 );
        myLuteceSearchUser.setGivenName( GIVENNAME1 );
        myLuteceSearchUser.setLastName( LASTNAME1 );
        myLuteceSearchUser.setEmail( EMAIL1 );

        // Create test
        MyLuteceSearchUserHome.create( myLuteceSearchUser );
        MyLuteceSearchUser myLuteceSearchUserStored = MyLuteceSearchUserHome.findByPrimaryKey( myLuteceSearchUser.getId( ) );
        assertEquals( myLuteceSearchUserStored.getLogin( ), myLuteceSearchUser.getLogin( ) );
        assertEquals( myLuteceSearchUserStored.getGivenName( ), myLuteceSearchUser.getGivenName( ) );
        assertEquals( myLuteceSearchUserStored.getLastName( ), myLuteceSearchUser.getLastName( ) );
        assertEquals( myLuteceSearchUserStored.getEmail( ), myLuteceSearchUser.getEmail( ) );

        // Update test
        myLuteceSearchUser.setLogin( LOGIN2 );
        myLuteceSearchUser.setGivenName( GIVENNAME2 );
        myLuteceSearchUser.setLastName( LASTNAME2 );
        myLuteceSearchUser.setEmail( EMAIL2 );
        MyLuteceSearchUserHome.update( myLuteceSearchUser );
        myLuteceSearchUserStored = MyLuteceSearchUserHome.findByPrimaryKey( myLuteceSearchUser.getId( ) );
        assertEquals( myLuteceSearchUserStored.getLogin( ), myLuteceSearchUser.getLogin( ) );
        assertEquals( myLuteceSearchUserStored.getGivenName( ), myLuteceSearchUser.getGivenName( ) );
        assertEquals( myLuteceSearchUserStored.getLastName( ), myLuteceSearchUser.getLastName( ) );
        assertEquals( myLuteceSearchUserStored.getEmail( ), myLuteceSearchUser.getEmail( ) );

        // List test
        MyLuteceSearchUserHome.getMyLuteceSearchUsersList( );

        // Delete test
        MyLuteceSearchUserHome.remove( myLuteceSearchUser.getId( ) );
        myLuteceSearchUserStored = MyLuteceSearchUserHome.findByPrimaryKey( myLuteceSearchUser.getId( ) );
        assertNull( myLuteceSearchUserStored );

    }

}
