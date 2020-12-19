import React from 'react';
import { SearchForm } from "./features/search/SearchForm";
import { SearchResults } from "./features/search/SearchResults";
import { DiseasePage } from "./features/disease/DiseasePage";
import { BrowserResults } from "./features/browser/BrowserResults";
import { BrowserRouter as Router, Route, Redirect } from "react-router-dom";
import Switch from "react-bootstrap/Switch";
import {NavbarD} from "./app/Navbar";



class App extends React.Component {
    render() {
        return (
            <div id="app" style={{overflow: "hidden", position: "relative", width: "100%"}}>
                <Router >
                    <NavbarD />
                    <Switch style={{ position:"absolute", height: "100%", width: "100%"}}>

                        {/*<Route exact path="/" render={() => (
                            <React.Fragment>
                                <SearchForm/>
                            </React.Fragment>
                        )}/>*/}
                        <Route exact path="/" component={ SearchForm }/>
                        <Route exact path="/disease/:omim" component={ DiseasePage }/>
                        <Route exact path="/searchResults" component={ SearchResults }/>
                        <Route exact path="/browse" component={ BrowserResults }/>
                        <Redirect to="/" />
                    </Switch>
                </Router>
            </div>
        );
    }
}

export default App;
