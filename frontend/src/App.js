import React from 'react';
import { SearchForm } from "./features/search/SearchForm";
import { SearchResults } from "./features/search/SearchResults";
import { DiseasePage } from "./features/disease/DiseasePage";
import { BrowserResults } from "./features/browser/BrowserResults";
import {BrowserRouter as Router, Route } from "react-router-dom";
import Switch from "react-bootstrap/Switch";
import {NavbarD} from "./app/Navbar";
import {AboutPage} from "./features/about/AboutPage";



class App extends React.Component {
    render() {
        return (
            <div id="app" style={{ position: "relative", width: "100%"}}>
                <Router basename={"/diseasecard"}>
                    <NavbarD />
                    <Switch style={{ position:"absolute", width: "100%", minHeight:"calc(100vh - 3.5em)"}}>
                        <Route exact path="/" component={ SearchForm }/>
                        <Route exact path="/disease/:omim" component={ DiseasePage }/>
                        <Route exact path="/searchResults" component={ SearchResults }/>
                        <Route exact path="/browse" component={ BrowserResults }/>
                        <Route exact path="/about" component={ AboutPage }/>
                        <Route exact path="/evaluateCard.do" component={ DiseasePage }/>
                    </Switch>
                </Router>
            </div>
        );
    }
}

export default App;
