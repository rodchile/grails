package org.codehaus.groovy.grails.web.sitemesh

import org.codehaus.groovy.grails.web.taglib.AbstractGrailsTagTests

/**
 * Tests the sitemesh capturing and rendering tags end-to-end
 *
 * @author Graeme Rocher
 * @since 1.2
 */

public class FullSitemeshLifeCycleTests extends AbstractGrailsTagTests{

    void testSimpleLayout() {
        def template = '''
<html>
		<head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>This is the title</title></head>
		<body onload="test();">body text</body>
</html>
'''

        assertOutputEquals '''
<html>
		<head><meta content="text/html; charset=UTF-8" http-equiv="Content-Type"/><title>This is the title</title></head>
		<body onload="test();">body text</body>
</html>
''', template
        
        def layout = '''
<html>
    <head><title>Decorated <g:layoutTitle /></title><g:layoutHead /></head>
    <body><h1>Hello</h1><g:layoutBody /></body>
</html>
'''
         def result = applyLayout(layout, template)

        assertEquals '''
<html>
    <head><title>Decorated This is the title</title><meta content="text/html; charset=UTF-8" http-equiv="Content-Type"/></head>
    <body><h1>Hello</h1>body text</body>
</html>
''', result
    }


    void testParameters() {
        def template = '''
<html>
  <head>
      <title>Simple GSP page</title>
      <meta name="layout" content="main"/>
      <parameter name="navigation" value="here!"/>
  </head>
  <body>Place your content here</body>
</html>
'''

        def layout = '<h1>pageProperty: ${pageProperty(name: \'page.navigation\')}</h1>'

        def result = applyLayout(layout, template)

        assertEquals '<h1>pageProperty: here!</h1>', result 
    }
}