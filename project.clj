;; Please don't bump the library version by hand - use ci.release-workflow instead.
(defproject com.nedap.staffing-solutions/utils.reverse "1.1.0"
  ;; Please keep the dependencies sorted a-z.
  :dependencies [[org.clojure/clojure "1.10.1"]]

  :description "Threading macros and HOFs for doing things in reverse order, increasing readability."

  :url "https://github.com/nedap/utils.reverse"

  :min-lein-version "2.0.0"

  :license {:name "EPL-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}

  :signing {:gpg-key "releases-staffingsolutions@nedap.com"}

  :repositories {"releases" {:url      "https://nedap.jfrog.io/nedap/staffing-solutions/"
                             :username :env/artifactory_user
                             :password :env/artifactory_pass}}

  :repository-auth {#"https://nedap.jfrog\.io/nedap/staffing-solutions/"
                    {:username :env/artifactory_user
                     :password :env/artifactory_pass}}

  :deploy-repositories {"clojars" {:url      "https://clojars.org/repo"
                                   :username :env/clojars_user
                                   :password :env/clojars_pass}}

  :target-path "target/%s"

  :source-paths ["src"]

  :test-paths ["test"]

  :resource-paths ["resources"]

  :monkeypatch-clojure-test false

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-pprint "1.1.2"]]

  ;; Please don't add `:hooks [leiningen.cljsbuild]`. It can silently skip running the JS suite on `lein test`.
  ;; It also interferes with Cloverage.
  :cljsbuild {:builds {"test" {:source-paths ["src" "test"]
                               :compiler     {:main          nedap.utils.reverse.test-runner
                                              :output-to     "target/out/tests.js"
                                              :output-dir    "target/out"
                                              :target        :nodejs
                                              :optimizations :none}}}}

  ;; A variety of common dependencies are bundled with `nedap/lein-template`.
  ;; They are divided into two categories:
  ;; * Dependencies that are possible or likely to be needed in all kind of production projects
  ;;   * The point is that when you realise you needed them, they are already in your classpath, avoiding interrupting your flow
  ;;   * After realising this, please move the dependency up to the top level.
  ;; * Genuinely dev-only dependencies allowing 'basic science'
  ;;   * e.g. criterium, deep-diff, clj-java-decompiler

  ;; NOTE: deps marked with #_"transitive" are there to satisfy the `:pedantic?` option.
  :profiles {:dev        {:dependencies [[cider/cider-nrepl "0.16.0" #_"formatting-stack needs it"]
                                         [com.clojure-goes-fast/clj-java-decompiler "0.2.1"]
                                         [com.nedap.staffing-solutions/speced.def "2.0.0"]
                                         [com.nedap.staffing-solutions/utils.modular "2.1.0"]
                                         [com.nedap.staffing-solutions/utils.spec.predicates "1.1.0"]
                                         [com.stuartsierra/component "0.4.0"]
                                         [com.taoensso/timbre "4.10.0"]
                                         [criterium "0.4.5"]
                                         [formatting-stack "4.2.0"]
                                         [lambdaisland/deep-diff "0.0-29"]
                                         [medley "1.2.0"]
                                         [org.clojure/core.async "1.0.567"]
                                         [org.clojure/math.combinatorics "0.1.1"]
                                         [org.clojure/test.check "1.0.0"]
                                         [org.clojure/tools.namespace "1.0.0"]
                                         [refactor-nrepl "2.4.0" #_"formatting-stack needs it"]]
                          :jvm-opts     ["-Dclojure.compiler.disable-locals-clearing=true"]
                          :plugins      [[lein-cloverage "1.1.1"]]
                          :source-paths ["dev"]
                          :repl-options {:init-ns dev}}

             :provided   {:dependencies [[org.clojure/clojurescript "1.10.597"
                                          :exclusions [com.cognitect/transit-clj
                                                       com.google.code.findbugs/jsr305
                                                       com.google.errorprone/error_prone_annotations]]
                                         [com.fasterxml.jackson.core/jackson-core "2.9.6" #_"transitive"]
                                         [com.google.guava/guava "25.1-jre" #_"transitive"]
                                         [com.google.protobuf/protobuf-java "3.4.0" #_"transitive"]
                                         [com.cognitect/transit-clj "0.8.313" #_"transitive"]
                                         [com.google.errorprone/error_prone_annotations "2.1.3" #_"transitive"]
                                         [com.google.code.findbugs/jsr305 "3.0.2" #_"transitive"]]}

             :check      {:global-vars {*unchecked-math* :warn-on-boxed
                                        ;; avoid warnings that cannot affect production:
                                        *assert*         false}}

             ;; some settings recommended for production applications.
             ;; You may also add :test, but beware of doing that if using this profile while running tests in CI
             ;; (since that would disable tests altogether)
             :production {:jvm-opts    ["-Dclojure.compiler.elide-meta=[:doc :file :author :line :column :added :deprecated :nedap.speced.def/spec :nedap.speced.def/nilable]"
                                        "-Dclojure.compiler.direct-linking=true"]
                          :global-vars {*assert* false}}

             ;; this profile is necessary since JDK >= 11 removes XML Bind, used by Jackson, which is a very common dep.
             :jdk11      {:dependencies [[javax.xml.bind/jaxb-api "2.3.1"]
                                         [org.glassfish.jaxb/jaxb-runtime "2.3.1"]]}

             :test       {:dependencies [[com.nedap.staffing-solutions/utils.test "1.6.2"]]
                          :jvm-opts     ["-Dclojure.core.async.go-checking=true"
                                         "-Duser.language=en-US"]}

             :ncrw       {:global-vars    {*assert* true} ;; `ci.release-workflow` relies on runtime assertions
                          :source-paths   ^:replace []
                          :test-paths     ^:replace []
                          :resource-paths ^:replace []
                          :plugins        ^:replace []
                          :dependencies   ^:replace [[com.nedap.staffing-solutions/ci.release-workflow "1.13.1"]]}

             :ci       {:pedantic?    :abort
                        :jvm-opts     ["-Dclojure.main.report=stderr"]}})
