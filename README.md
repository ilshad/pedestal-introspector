# pedestal-introspector

This library provides tool for visualize application state while
developing ClojureScript-based projects with Pedestal. Press
`Ctrl+i` in browser and see current data model in popup window.

## Current state

Currently, this tool visualizes:

 1. Data model

## Installation

Leiningen coordinates:

```clojure
[ilshad/pedestal-introspector "x.x.x"]
```

!NOTE: yet not released.

## Usage

**Step One:** Create introspector with app in `start.cljs`:

```clojure
(ns orgto-client.start
  (:require ...
            [ilshad.pedestal-introspector :as introspector]
			...))

(defn ^:export main []
  (let [app (create-app (render-core/render-config))]
    (introspector/create app)))
```

**Step Two:** Enable keyboard shortcut somewhere in rendering code.
For example:

```clojure
(ns orgto-client.rendering
  (:require ...
            [ilshad.pedestal-introspector :as introspector]
			...))

(defn render-main [_ _ _]
  ...
  (introspector/bind-key)
  ...
  )

(defn render-config []
  [[:node-create [:main] render-main]
  ...
  ])
```

By default, keybinding is `Ctrl+i`. Press it in browser while working
with Development Aspect and see curent data model.

## Alternative ways

Instead of keyboard shortcut, you can call popup window manually. Type
in JavaScript console:

    ilshad.pedestal_introspector.open()

Or call `(ilshad.pedestal-introspector/open)` somewhere in ClojureScript.

## License

Copyright © 2013 [Ilshad Khabibullin](http://ilshad.com).

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
