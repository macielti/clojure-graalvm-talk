Minimalist slides for Scittle ClojureScript.

# quickstart

- Clone this repository
- Edit [`slides.cljs`](./slides.cljs) and add your slides.
- Open [`index.html`](./index.html) in your browser.

To get a live-reloading dev experience you can [Start a `josh` server](#dev).

# slides

Each slide is a `:section` tag like this:

```clojure
(defn slides [state]
  [:<>
   [:main

    [:section
     [:h1 "Hello"]
     [:h2 "Your first slide."]]

    [:section
     [:h1 "Slide Two"]
     [:img {:src "https://w.wiki/CAvg"}]
     [:h3 "It's the moon."]]

     ; ...
```

# navigation

Slide navigation keys:

- Next: RightArrow, DownArrow, PageDown, Spacebar, Enter
- Prev: LeftArrow, UpArrow, PageUp
- First: Home, Escape, Q
- Last: End

Or tap/click the right/left side of the screen to go foward/backward.

# features

- Easy to deploy static HTML.
- Use hiccup Reagent forms to design slides.
- Tiny hackable codebase.

# dev

Start a live-reloading dev server:

```
echo {} > package.json
npm i cljs-josh
npx josh
```

(Or just `josh` if you have done `npm i -g cljs-josh` to install it globally).

# about

Built at Barcamp London 2024 for [this talk](https://chr15m.github.io/barcamp-whats-the-point-of-lisp/).
