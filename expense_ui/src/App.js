import React, { Component } from 'react';
import './App.css';
import { Switch,Route } from 'react-router-dom'
import Login from './components/login/Login';
import Home from './components/home/Home';
import {ACCESS_TOKEN} from './constants'
import PrivateRoute from './common/PrivateRouter';

class App extends Component{

  constructor(props){
    super(props);
      this.state ={
        authenticated: false,
        currentUser: null
      }
    this.loadCurrentlyLoggedInUser = this.loadCurrentlyLoggedInUser.bind(this);
  }

  loadCurrentlyLoggedInUser() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
      return Promise.reject("No access token set.");
    }else{
      this.setState({
        authenticated: true,
        currentUser: localStorage.getItem(ACCESS_TOKEN)
      });
    }
    console.log(this.state.authenticated);
  }

  componentDidMount() {
    this.loadCurrentlyLoggedInUser();
  }

  render(){
    return (
      <div className="app">
        <div className="app-body">
          <Switch>
            <Route exact path="/" component={Login} />
            <Route exact path="/login" component={Login} />
            <PrivateRoute path="/home" authenticated={this.state.authenticated} currentUser={this.state.currentUser} component={Home}></PrivateRoute>
          </Switch>
        </div>
      </div>
    );
  }
}

export default App;
