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
package fr.paris.lutece.plugins.mylutece.modules.users.service.search;

import fr.paris.lutece.portal.service.search.IndexationService;
import fr.paris.lutece.portal.service.search.LuceneSearchEngine;
import fr.paris.lutece.portal.service.search.SearchItem;
import fr.paris.lutece.portal.service.util.AppLogService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

/**
 * 
 * LocalUserSearchEngine
 * 
 */
public class LocalUserSearchEngine
{
    /**
     * Return search results
     * 
     * @param strKeywords
     *            Keywords
     * @param request
     *            The HTTP request
     * @return Results as a collection of SearchResult
     */
    public static List<Map<String, String>> getSearchResults( String strKeywords, HttpServletRequest request )
    {
        List<Map<String, String>> listResults = new ArrayList<>( );
        IndexSearcher searcher = null;
        try ( Directory directory = IndexationService.getDirectoryIndex( ) ; IndexReader ir = DirectoryReader.open( directory ) )
        {
            searcher = new IndexSearcher( ir );
            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder( );

            if ( ( strKeywords != null ) && !strKeywords.equals( "" ) )
            {
                QueryParser parser = new QueryParser( SearchItem.FIELD_CONTENTS, IndexationService.getAnalyser( ) );
                queryBuilder.add( parser.parse( strKeywords ), BooleanClause.Occur.MUST );
            }

            TopDocs topDocs = searcher.search( queryBuilder.build( ), LuceneSearchEngine.MAX_RESPONSES );
            ScoreDoc [ ] hits = topDocs.scoreDocs;
            for ( int i = 0; i < hits.length; i++ )
            {
                int docId = hits [i].doc;
                Document document = searcher.doc( docId );
                Map<String, String> listUserResults = new HashMap<>( );
                for ( IndexableField field : document.getFields( ) )
                {

                    listUserResults.put( field.name( ), field.stringValue( ) );

                }
                listResults.add( listUserResults );
            }
        }
        catch( Exception e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        return listResults;
    }

}
