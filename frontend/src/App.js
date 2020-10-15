import React from 'react';
import './App.css';
import NavbarD from "./components/Navbar";
import Search from "./components/Search";


class App extends React.Component {

    render() {
        return (
            <div id="app">
                <NavbarD />
                <Search />
            </div>
        );
    }
}

export default App;
