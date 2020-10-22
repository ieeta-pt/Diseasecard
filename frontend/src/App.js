import React from 'react';
import NavbarD from "./app/Navbar";
import { SearchForm } from "./features/search/SearchForm";
import { SearchResults } from "./features/search/SearchResults";
import { BrowserRouter as Router, Route, Redirect } from "react-router-dom";
import Switch from "react-bootstrap/Switch";


class App extends React.Component {

    render() {
        return (
            <div id="app">
                <Router>
                    <NavbarD />
                    <Switch>
                        {/*<Route exact path="/" render={() => (
                            <React.Fragment>
                                <SearchForm/>
                            </React.Fragment>
                        )}/>*/}
                        <Route exact path="/" component={ SearchForm }/>
                        <Route exact path="/searchResults" component={ SearchResults }/>
                        <Redirect to="/" />
                    </Switch>
                </Router>

            </div>
        );
    }
}

export default App;
