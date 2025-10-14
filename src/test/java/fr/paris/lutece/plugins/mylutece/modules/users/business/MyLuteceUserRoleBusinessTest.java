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

import fr.paris.lutece.test.LuteceTestCase;

/**
 * This is the business class test for the object MyLuteceUserRole
 */
public class MyLuteceUserRoleBusinessTest extends LuteceTestCase
{
    private static final int IDMYLUTECESEARCHUSER1 = 1;
    private static final int IDMYLUTECESEARCHUSER2 = 2;
    private static final String STRROLE1 = "ROLE1";
    private static final String STRROLE2 = "ROLE2";

    /**
     * test MyLuteceUserRole
     */
    public void testBusiness( )
    {
        // Initialize an object
        MyLuteceUserRole myLuteceUserRole = new MyLuteceUserRole( );
        myLuteceUserRole.setIdMyLuteceSearchUser( IDMYLUTECESEARCHUSER1 );
        myLuteceUserRole.setRoleKey( STRROLE1 );

        // Create test
        MyLuteceUserRoleHome.create( myLuteceUserRole );
        MyLuteceUserRole myLuteceUserRoleStored = MyLuteceUserRoleHome.findByPrimaryKey( myLuteceUserRole.getId( ) );
        assertEquals( myLuteceUserRoleStored.getIdMyLuteceSearchUser( ), myLuteceUserRole.getIdMyLuteceSearchUser( ) );
        assertEquals( myLuteceUserRoleStored.getRoleKey( ), myLuteceUserRole.getRoleKey( ) );

        // Update test
        myLuteceUserRole.setIdMyLuteceSearchUser( IDMYLUTECESEARCHUSER2 );
        myLuteceUserRole.setRoleKey( STRROLE2 );
        MyLuteceUserRoleHome.update( myLuteceUserRole );
        myLuteceUserRoleStored = MyLuteceUserRoleHome.findByPrimaryKey( myLuteceUserRole.getId( ) );
        assertEquals( myLuteceUserRoleStored.getIdMyLuteceSearchUser( ), myLuteceUserRole.getIdMyLuteceSearchUser( ) );
        assertEquals( myLuteceUserRoleStored.getRoleKey( ), myLuteceUserRole.getRoleKey( ) );

        // List test
        MyLuteceUserRoleHome.getMyLuteceUserRolesList( );

        // Delete test
        MyLuteceUserRoleHome.remove( myLuteceUserRole.getId( ) );
        myLuteceUserRoleStored = MyLuteceUserRoleHome.findByPrimaryKey( myLuteceUserRole.getId( ) );
        assertNull( myLuteceUserRoleStored );

    }

}
