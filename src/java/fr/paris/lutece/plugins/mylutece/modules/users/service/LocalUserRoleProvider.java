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
package fr.paris.lutece.plugins.mylutece.modules.users.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import fr.paris.lutece.plugins.mylutece.modules.users.business.LocalUser;
import fr.paris.lutece.plugins.mylutece.modules.users.business.LocalUserHome;
import fr.paris.lutece.plugins.mylutece.modules.users.business.LocalUserRole;
import fr.paris.lutece.plugins.mylutece.modules.users.business.LocalUserRoleHome;
import fr.paris.lutece.plugins.mylutece.service.IMyLuteceExternalRolesProvider;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public final class LocalUserRoleProvider implements IMyLuteceExternalRolesProvider {
    private static final String BEAN_NAME = "mylutece-localUserRoleProvider";
    private static LocalUserRoleProvider _localUserRoleProvider;

    /**
     * Default constructor
     */
    private LocalUserRoleProvider() {
    }

    public void init() {
        _localUserRoleProvider = SpringContextService.getBean(BEAN_NAME);
    }

    /**
     * Returns the instance of the singleton
     *
     * @return The instance of the singleton
     */
    public static LocalUserRoleProvider getInstance() {
        if (_localUserRoleProvider == null) {
            _localUserRoleProvider = SpringContextService.getBean(BEAN_NAME);
        }
        return _localUserRoleProvider;
    }

    @Override
    public Collection<String> providesRoles(LuteceUser user) {
        Collection<String> providesRoles = new ArrayList<String>();
        LocalUser localUser = LocalUserHome.findByConnectId(user.getName());
        List<LocalUserRole> localUserRoleList = LocalUserRoleHome.getLocalUserRolesListByUserId(localUser.getId());
        for (LocalUserRole localUserRole : localUserRoleList) {
            providesRoles.add(localUserRole.getRoleKey());
        }
        return providesRoles;
    }
}
