(defproject com.nedap.staffing-solutions/utils.reverse "0.1.0"
  :description "Threading macros and HOFs for doing things in reverse order, increasing readability."
  :url "https://github.com/nedap/utils.reverse"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories {"releases"       {:url      "https://nedap.jfrog.io/nedap/staffing-solutions/"
                                   :username :env/artifactory_user
                                   :password :env/artifactory_pass}}
  :deploy-repositories [["releases" {:url "https://nedap.jfrog.io/nedap/staffing-solutions/"
                                     :sign-releases false}]]
  :repository-auth {#"https://nedap.jfrog\.io/nedap/staffing-solutions/"
                    {:username :env/artifactory_user
                     :password :env/artifactory_pass}}
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :profiles {:dev {:global-vars {*warn-on-reflection* true}}})
