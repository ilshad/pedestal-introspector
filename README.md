# pedestal-introspector

This library provides tool for visualization application state while
developing ClojureScript-based projects with
[Pedestal](http://pedestal.io). In browser, press `Ctrl+I`
and see current data model in popup window:

![Screenshot](screenshot.png)

## Current state

Currently, it visualizes:

 1. Data model

## Installation

Leiningen coordinates:

```clojure
[ilshad/pedestal-introspector "0.1.0"]
```

## Usage

**Step One:** Create introspector with app in `start.cljs`:

```clojure
(ns myapp.start
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
(ns myapp.rendering
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

By default, keybinding is `Ctrl+I`. Press it in browser while working
with Development Aspect and see curent data model.

## Alternative ways

Instead of keyboard shortcut, you can call popup window manually. Type
in JavaScript console:

    ilshad.pedestal_introspector.open()

Or call `(ilshad.pedestal-introspector/open)` somewhere in ClojureScript.

## Known issues

Google Chrome browser is only supported currently (version 0.1.0).

## License

Copyright © 2013 [Ilshad Khabibullin](http://ilshad.com).

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
