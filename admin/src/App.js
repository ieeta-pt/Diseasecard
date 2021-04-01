import React, { Component, Suspense } from 'react';
import { Switch, Route } from 'react-router-dom';
import Loadable from 'react-loadable';


import routes from "./routes";
import Aux from "./template/aux";
import Loader from "./template/layout/Loader"
import ScrollToTop from "./template/layout/ScrollToTop";


const AdminLayout = Loadable({
  loader: () => import('./template/layout/AdminLayout'),
  loading: Loader
});

class App extends Component {
  render() {
    const menu = routes.map((route, index) => {
      return (route.component) ? (
          <Route
              key={index}
              path={route.path}
              exact={route.exact}
              name={route.name}
              render={props => (
                  <route.component {...props} />
              )} />
      ) : null;
    });

    return (
        <Aux>
          <ScrollToTop>
            <Suspense fallback={<Loader/>}>
              <Switch>
                <Route path="/" component={AdminLayout} />
              </Switch>
            </Suspense>
          </ScrollToTop>
        </Aux>
    );
  }
}

export default App;
