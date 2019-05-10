package grails.plugin.springsecurity.oauth2.vk

import grails.plugin.springsecurity.ReflectionUtils
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.oauth2.SpringSecurityOauth2BaseService
import grails.plugin.springsecurity.oauth2.exception.OAuth2Exception
import grails.plugins.Plugin
import org.slf4j.LoggerFactory

class SpringSecurityOauth2VkGrailsPlugin extends Plugin {

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.1.8 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]
    List loadAfter = ['spring-security-oauth2']

    // TODO Fill in these fields
    def title = "Spring Security Oauth2 VK Provider" // Headline display name of the plugin
    def author = "purpleraven"
    def authorEmail = "chartgizmo@gmail.com"
    def description = '''\
This plugin provides the capability to authenticate via VK-oauth provider. Depends on grails-spring-security-oauth2.
'''
    def profiles = ['web']

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/grails-spring-security-oauth2-vk"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
//    def scm = [ url: "http://svn.codehaus.org/grails-plugins/" ]
    def log

    Closure doWithSpring() {
        { ->
            ReflectionUtils.application = grailsApplication
            if (grailsApplication.warDeployed) {
                SpringSecurityUtils.resetSecurityConfig()
            }
            SpringSecurityUtils.application = grailsApplication

            // Check if there is an SpringSecurity configuration
            def coreConf = SpringSecurityUtils.securityConfig
            boolean printStatusMessages = (coreConf.printStatusMessages instanceof Boolean) ? coreConf.printStatusMessages : true
            if (!coreConf || !coreConf.active) {
                if (printStatusMessages) {
                    println("ERROR: There is no SpringSecurity configuration or SpringSecurity is disabled")
                    println("ERROR: Stopping configuration of SpringSecurity Oauth2")
                }
                return
            }

            if (!hasProperty('log')) {
                log = LoggerFactory.getLogger(SpringSecurityOauth2VkGrailsPlugin)
            }

            if (printStatusMessages) {
                println("Configuring Spring Security OAuth2 vk plugin...")
            }
            SpringSecurityUtils.loadSecondaryConfig('DefaultOAuth2VkConfig')
            if (printStatusMessages) {
                println("... finished configuring Spring Security VK\n")
            }
        }
    }

    @Override
    void doWithApplicationContext() {
        log.trace("doWithApplicationContext")
        def SpringSecurityOauth2BaseService oAuth2BaseService = grailsApplication.mainContext.getBean('springSecurityOauth2BaseService') as SpringSecurityOauth2BaseService
        def VkOAuth2Service vkOAuth2Service = grailsApplication.mainContext.getBean('vkOAuth2Service') as VkOAuth2Service
        try {
            oAuth2BaseService.registerProvider(vkOAuth2Service)
        } catch (OAuth2Exception exception) {
            log.error("There was an oAuth2Exception", exception)
            log.error("OAuth2 VK not loaded")
        }
    }
}
