package org.codehaus.groovy.grails.web.pages

import grails.test.AbstractGrailsEnvChangingSpec

import spock.lang.Specification
import grails.test.mixin.TestFor
import grails.util.Environment;
import grails.artefact.Artefact


@TestFor(CustomApplicationTagLib)
class InvokeTagWithCustomBodyClosureSpec extends AbstractGrailsEnvChangingSpec {
    def "Test that a custom tag library can invoke another tag with a closure body"(grailsEnv) {
        when:'We call a custom tag that invokes an existing tag with a closure body'
            changeGrailsEnv(grailsEnv)
            def content = applyTemplate("<a:myLink />")
            def content2 = applyTemplate("<a:myLink />")
        then:"The expected result is rendered and when we call it a second time the cached version is used so we test that too"
            content == '<a href="/one/two"></a><a href="/foo/bar">Hello World</a>'
            content == content2
        where:
            grailsEnv << grailsEnvs
    }

    def 'Test invoking a tag with and then without attributes'() {
        when:
            def content = applyTemplate("<a:myLink foo='bar'/><a:myLink/>")

        then:
            content == '<a href="/one/two"></a><a href="/foo/bar">Hello World</a><a href="/one/two"></a><a href="/foo/bar">Hello World</a>'
    }
}
@Artefact("TagLibrary")
class CustomApplicationTagLib {
    static namespace = "a"
    def myLink = { attrs, body ->
       out << g.link(controller:"one", action:"two")
       out << g.link(controller: "foo", action:"bar") {
           setLink(attrs, "World")
       }
    }

    private setLink(attrs, name) {
        "Hello $name"
    }
}
