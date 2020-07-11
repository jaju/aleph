(def netty-version "4.1.50.Final")

(def netty-modules
  '[transport
    transport-native-epoll
    codec
    codec-http
    handler
    handler-proxy
    resolver
    resolver-dns])

(def other-dependencies
  '[[org.clojure/tools.logging "1.1.0" :exclusions [org.clojure/clojure]]
    [manifold "0.1.9-alpha3"]
    [byte-streams "0.2.5-alpha2"]
    [potemkin "0.4.5"]])

(defproject msync/aleph "0.4.7-alpha6"
  :description "a framework for asynchronous communication"
  :repositories {"jboss" "https://repository.jboss.org/nexus/content/groups/public/"
                 "sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}
  :license {:name "MIT License"}
  :dependencies ~(concat
                   other-dependencies
                   (map
                     #(vector (symbol "io.netty" (str "netty-" %)) netty-version)
                     netty-modules))
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.10.1"]
                                  [criterium "0.4.4"]
                                  [cheshire "5.10.0"]
                                  [org.slf4j/slf4j-simple "1.7.30"]
                                  [com.cognitect/transit-clj "1.0.324"]]}}
  :codox {:src-dir-uri "https://github.com/jaju/aleph/tree/master/"
          :src-linenum-anchor-prefix "L"
          :defaults {:doc/format :markdown}
          :include [aleph.tcp
                    aleph.udp
                    aleph.http
                    aleph.flow]
          :output-dir "doc"}
  :plugins [[lein-codox "0.10.7"]
            [lein-jammin "0.1.1"]
            [lein-marginalia "0.9.1"]
            [lein-cljfmt "0.6.8"]]
  :java-source-paths ["src"]
  :javac-options ["-target" "1.8", "-source" "1.8"]
  :cljfmt {:indents {#".*" [[:inner 0]]}}
  :test-selectors {:default #(not
                               (some #{:benchmark :stress}
                                 (cons (:tag %) (keys %))))
                   :benchmark :benchmark
                   :stress :stress
                   :all (constantly true)}
  :jvm-opts ^:replace ["-server"
                       #_"-Xmx256m"
                       "-Xmx2g"
                       "-XX:+HeapDumpOnOutOfMemoryError"
                       #_"-XX:+PrintCompilation"
                       #_"-XX:+UnlockDiagnosticVMOptions"
                       #_"-XX:+PrintInlining"]
  :global-vars {*warn-on-reflection* true})
