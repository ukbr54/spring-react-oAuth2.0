import React,{Component} from 'react';
import './Login.css';
import fbLogo from '../../images/fb-logo.png';
import googleLogo from '../../images/google-logo.png';
import githubLogo from '../../images/github-logo.png';
import Alert from 'react-s-alert';
import { login } from '../../utils/APIUtil';
import {GOOGLE_AUTH_URL, FACEBOOK_AUTH_URL, GITHUB_AUTH_URL, ACCESS_TOKEN } from '../../constants';

class Login extends Component{
    constructor(props){
        super(props);
        console.log("Constructor: LoginComponent")
    }

    render(){
        return(
            <div className = "login-container">
                <div className="login-content">
                    <h1 className = "login-title">Login to SpringSocial</h1>
                    <SocialLogin />
                    <div className="or-separator">
                        <span className="or-text">OR</span>
                    </div>
                    <LoginForm {...this.props} />
                </div>
            </div>
        )
    }
}

class SocialLogin extends Component{
    render(){
        return(
            <div className = "social-login">
                <a className="btn btn-block social-btn google" href={GOOGLE_AUTH_URL}>
                    <img src={googleLogo} alt="Google" /> Log in with Google
                </a>
                <a className="btn btn-block social-btn facebook" href={FACEBOOK_AUTH_URL}>
                    <img src={fbLogo} alt="Facebook" /> Log in with Facebook
                </a>
                <a className="btn btn-block social-btn github" href={GITHUB_AUTH_URL}>
                    <img src={githubLogo} alt="Github" /> Log in with Github
                </a>
            </div>    
        )
    }
}

class LoginForm extends Component{
    constructor(props){
        super(props);
        this.state = {
            email: '',
            password: ''
        };

        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleInputChange(event){
        const target = event.target;
        const inputName = target.name;
        const inputValue = target.value;

        this.setState({
            [inputName] : inputValue
        });
    }

    handleSubmit(event) {
        event.preventDefault();   

        const loginRequest = Object.assign({}, this.state);

        login(loginRequest).then(response => {
            console.log(response.accessToken);
            localStorage.setItem(ACCESS_TOKEN, response.accessToken);
            Alert.success("You're successfully logged in!");
            this.props.history.push("/home");
        }).catch(error => {
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        });
    }

    render(){
        return(
            <form onSubmit={this.handleSubmit}>
                <div className="form-item">
                    <input type="email" name="email" className="form-control" 
                           placeholder="Email" value={this.state.email} onChange={this.handleInputChange} required/>
                </div>
                <div className="form-item">
                    <input type="password" name="password" className="form-control"
                           placeholder="Password" value={this.state.password} onChange={this.handleInputChange} required/>
                </div>
                <div className="form-item">
                    <button type="submit" className="btn btn-block btn-primary">Login</button>
                </div>
            </form>
        )
    }
}

export default Login