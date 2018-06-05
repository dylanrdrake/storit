(ns client.dash)


(def menu-burger  (.getElementById js/document "hamburger"))
(def contents     (.getElementById js/document "contents"))
(def tbl-ctrl-bar (.getElementById js/document "tbl-ctrl-bar"))
(def side-bar     (.getElementById js/document "side-bar"))


(defn show-mobile-menu
  []
  (do (set! menu-burger.style.display "none")
      (set! contents.style.display "none")
      (set! tbl-ctrl-bar.style.display "none")
      (set! side-bar.style.display "block")))


(defn add-click-event
  [el]
    (.addEventListener el "click" show-mobile-menu))


(defn setup
  []
  (do (add-click-event menu-burger)))


(setup)

