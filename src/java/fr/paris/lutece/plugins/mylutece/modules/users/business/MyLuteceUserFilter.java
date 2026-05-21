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
package fr.paris.lutece.plugins.mylutece.modules.users.business;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.url.UrlItem;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Filter for the front-office users search engine (last name, first name, email).
 */
public class MyLuteceUserFilter implements Serializable
{
    private static final long serialVersionUID = 1L;

    private static final String EQUAL = "=";
    private static final String AMPERSAND = "&";

    private static final String PARAMETER_SEARCH_LAST_NAME = "search_last_name";
    private static final String PARAMETER_SEARCH_FIRST_NAME = "search_first_name";
    private static final String PARAMETER_SEARCH_EMAIL = "search_email";
    private static final String PARAMETER_SEARCH_IS_SEARCH = "search_is_search";

    private static final String PROPERTY_ENCODING_URL = "lutece.encoding.url";

    private String _strLastName;
    private String _strFirstName;
    private String _strEmail;

    /**
     * Reset every criterion to an empty string.
     */
    public void init( )
    {
        _strLastName = "";
        _strFirstName = "";
        _strEmail = "";
    }

    /**
     * @return the last name criterion
     */
    public String getLastName( )
    {
        return _strLastName;
    }

    /**
     * @param strLastName
     *            the last name criterion
     */
    public void setLastName( String strLastName )
    {
        _strLastName = strLastName;
    }

    /**
     * @return the first name criterion
     */
    public String getFirstName( )
    {
        return _strFirstName;
    }

    /**
     * @param strFirstName
     *            the first name criterion
     */
    public void setFirstName( String strFirstName )
    {
        _strFirstName = strFirstName;
    }

    /**
     * @return the email criterion
     */
    public String getEmail( )
    {
        return _strEmail;
    }

    /**
     * @param strEmail
     *            the email criterion
     */
    public void setEmail( String strEmail )
    {
        _strEmail = strEmail;
    }

    /**
     * Populate the filter from the given HTTP request. Reset all criteria when the search sentinel parameter is absent.
     *
     * @param request
     *            the HTTP request
     * @return true if the request triggered a search (sentinel parameter present), false otherwise
     */
    public boolean setMyLuteceUserFilter( HttpServletRequest request )
    {
        boolean bIsSearch = request.getParameter( PARAMETER_SEARCH_IS_SEARCH ) != null;
        if ( bIsSearch )
        {
            _strLastName = request.getParameter( PARAMETER_SEARCH_LAST_NAME );
            _strFirstName = request.getParameter( PARAMETER_SEARCH_FIRST_NAME );
            _strEmail = request.getParameter( PARAMETER_SEARCH_EMAIL );
        }
        else
        {
            init( );
        }
        return bIsSearch;
    }

    /**
     * Append the filter parameters to the given URL.
     *
     * @param url
     *            the URL to enrich
     */
    public void setUrlAttributes( UrlItem url )
    {
        url.addParameter( PARAMETER_SEARCH_IS_SEARCH, Boolean.TRUE.toString( ) );
        try
        {
            url.addParameter( PARAMETER_SEARCH_LAST_NAME,
                    URLEncoder.encode( _strLastName == null ? "" : _strLastName, AppPropertiesService.getProperty( PROPERTY_ENCODING_URL ) ) );
            url.addParameter( PARAMETER_SEARCH_FIRST_NAME,
                    URLEncoder.encode( _strFirstName == null ? "" : _strFirstName, AppPropertiesService.getProperty( PROPERTY_ENCODING_URL ) ) );
            url.addParameter( PARAMETER_SEARCH_EMAIL,
                    URLEncoder.encode( _strEmail == null ? "" : _strEmail, AppPropertiesService.getProperty( PROPERTY_ENCODING_URL ) ) );
        }
        catch( UnsupportedEncodingException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
    }

    /**
     * @return the filter parameters as an URL-encoded query string
     */
    public String getUrlAttributes( )
    {
        StringBuilder sb = new StringBuilder( );
        sb.append( PARAMETER_SEARCH_IS_SEARCH ).append( EQUAL ).append( Boolean.TRUE );
        try
        {
            sb.append( AMPERSAND ).append( PARAMETER_SEARCH_LAST_NAME ).append( EQUAL )
                    .append( URLEncoder.encode( _strLastName == null ? "" : _strLastName, AppPropertiesService.getProperty( PROPERTY_ENCODING_URL ) ) );
            sb.append( AMPERSAND ).append( PARAMETER_SEARCH_FIRST_NAME ).append( EQUAL )
                    .append( URLEncoder.encode( _strFirstName == null ? "" : _strFirstName, AppPropertiesService.getProperty( PROPERTY_ENCODING_URL ) ) );
            sb.append( AMPERSAND ).append( PARAMETER_SEARCH_EMAIL ).append( EQUAL )
                    .append( URLEncoder.encode( _strEmail == null ? "" : _strEmail, AppPropertiesService.getProperty( PROPERTY_ENCODING_URL ) ) );
        }
        catch( UnsupportedEncodingException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        return sb.toString( );
    }
}
