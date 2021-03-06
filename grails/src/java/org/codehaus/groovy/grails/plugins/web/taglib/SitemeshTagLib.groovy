/* Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT c;pWARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codehaus.groovy.grails.plugins.web.taglib

import org.codehaus.groovy.grails.web.sitemesh.GSPSitemeshPage
import org.codehaus.groovy.grails.web.util.StreamCharBuffer
import org.codehaus.groovy.grails.web.pages.FastStringWriter
import org.codehaus.groovy.grails.web.sitemesh.GrailsPageFilter
import com.opensymphony.module.sitemesh.RequestConstants
import org.apache.commons.lang.WordUtils

/**
 * Tag library containing internal Sitemesh pre-processor tags
 * 
 * @author Graeme Rocher
 * @since 1.2
 */

public class SitemeshTagLib implements RequestConstants {

    static namespace = 'sitemesh'

    def captureTagContent(writer, tagname, attrs, body) {
    	def content=null
    	if(body != null) {
    		if(body instanceof Closure) {
    			content = body()
    		} else {
    			content = body
    		}
    	}
    	writer << "<"
    	writer << tagname
    	if(attrs) {
    		attrs.each { k, v ->
    			writer << " ${k}=\"${v.encodeAsHTML()}\""
    		}
    	}
    	if(content) {
	    	writer << ">$content</$tagname>"
    	} else {
    		writer << "/>"
    	}
    	content
    }

    def wrapContentInBuffer(content) {
		if(content instanceof Closure) {
			content = content()
		}
    	if(!(content instanceof StreamCharBuffer)) {
        	// the body closure might be a string constant, so wrap it in a StreamCharBuffer in that case
    		def newbuffer=new FastStringWriter()
    		newbuffer.print(content)
    		content=newbuffer.buffer
    	}
    	content
    }

    /**
     * Used to capture the <head> tag
     */
    def captureHead = { attrs, body ->
    	def content=captureTagContent(out, 'head', attrs, body)

    	if(content != null) {
            // strip out title for sitemesh version of <head>
            content = content.replaceFirst(/<title(\s[^>]*)?>(.*?)<\/title>/,'')
    		GSPSitemeshPage smpage=request[GrailsPageFilter.GSP_SITEMESH_PAGE]
            if(smpage) {
            	smpage.setHeadBuffer(wrapContentInBuffer(content))
            }
    	}
    }

    /**
     * Allows passing of parameters to Sitemesh layout
     *
     * <sitemesh:parameter name="foo" value="bar" />
     */
    def parameter = { attrs, body ->
        GSPSitemeshPage smpage=request[GrailsPageFilter.GSP_SITEMESH_PAGE]
        def name = attrs.name?.toString()
        def val = attrs.value?.toString()
        if(smpage && name && val) {
            smpage.addProperty("page.$name", val)
        }
    }

    /**
     * Used to capture the <body> tag
     */
    def captureBody = { attrs, body ->
		def content=captureTagContent(out, 'body', attrs, body)
		if(content != null) {
    		GSPSitemeshPage smpage=request[GrailsPageFilter.GSP_SITEMESH_PAGE]
            if(smpage) {
            	smpage.setBodyBuffer(wrapContentInBuffer(content))
		    	if(attrs) {
		    		attrs.each { k, v ->
		    			smpage.addProperty("body.${k.toLowerCase()}", v?.toString())
		    		}
		    	}
            }
		}
    }

    /**
     * Used to capture the individual <content> tags
     */
    def captureContent = { attrs, body ->
		if(body != null) {
			GSPSitemeshPage smpage=request[GrailsPageFilter.GSP_SITEMESH_PAGE]
	        if(smpage && attrs.tag) {
	        	smpage.setContentBuffer(attrs.tag, wrapContentInBuffer(body))
	        }
		}
	}

    /**
     * Used to capture the individual <meta> tags
     */
    def captureMeta = { attrs, body ->
    	def content=captureTagContent(out, 'meta', attrs, body)
   		GSPSitemeshPage smpage=request[GrailsPageFilter.GSP_SITEMESH_PAGE]
    	if(attrs && smpage) {
    		if(attrs.name) {
    			smpage.addProperty("meta.${attrs.name}", attrs.content)
    			smpage.addProperty("meta.${attrs.name.toLowerCase()}", attrs.content)
    		} else if (attrs['http-equiv']) {
    			smpage.addProperty("meta.http-equiv.${attrs['http-equiv']}", attrs.content)
    			smpage.addProperty("meta.http-equiv.${attrs['http-equiv'].toLowerCase()}", attrs.content)
    			smpage.addProperty("meta.http-equiv.${WordUtils.capitalize(attrs['http-equiv'],['-'] as char[])}", attrs.content)
        	}
    	}
    }

    /**
     * Used to capture the <title> tag
     */
    def captureTitle = { attrs, body ->
    	def content=captureTagContent(out, 'title', attrs, body)
    	GSPSitemeshPage smpage=request[GrailsPageFilter.GSP_SITEMESH_PAGE]
    	if(smpage && content != null) {
    		smpage.addProperty('title', content?.toString())
    	}
    }
    
}