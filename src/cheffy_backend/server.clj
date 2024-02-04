(ns cheffy-backend.server
  (:require
   [environ.core :refer [env]]
   [integrant.core :as ig]
   [reitit.ring :as ring]
   [ring.adapter.jetty :as jetty])
  (:gen-class))

(defn app [env]
  (ring/ring-handler
    (ring/router
      [["/" {:get {:handler (fn [req]
                              {:status 200
                               :body "Hello World"})}}]])))

(defmethod ig/prep-key :server/jetty
  [_ config]
  (merge config {:port (parse-long (or (env :port) "4000"))}))

(defmethod ig/init-key :server/jetty
  [_ {:keys [handler port]}]
  (println "\n Server running on port" port)
  (jetty/run-jetty handler {:port port
                            :join? false}))

(defmethod ig/init-key :cheffy/app
  [_ config]
  (println "\n Started app")
  (app config))

(defmethod ig/init-key :db/postgres 
  [_ config]
  (println "\n Configured db")
  (:jdbc-url config))
    
(defmethod ig/halt-key! :server/jetty
  [_ jetty]
  (.stop jetty))

(defn -main [config-file]
  (let [config (-> config-file slurp ig/read-string)]
    (-> config ig/prep ig/init)))

(comment
  (-main "resources/config.edn"))
