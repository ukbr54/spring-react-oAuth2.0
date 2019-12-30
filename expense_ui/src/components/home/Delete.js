import React from 'react';
import axios from 'axios';
import { Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import {ACCESS_TOKEN} from '../../constants'

class Delete extends React.Component{
    constructor(){
        super();
        this.state={
            id: '', 
            month: '',
            year: ''
        };
        this.onClick = this.onClick.bind(this);
        this.delete = this.delete.bind(this);
    }

    componentDidMount() {
        this.setState({
          id: this.props.expense.id,
          month: this.props.expense.month,
          year: this.props.expense.year
        })
    }

    componentWillReceiveProps(nextProps){
      this.setState({
        id: nextProps.expense.id,
        month:nextProps.expense.month,
        year:nextProps.expense.year
      })
    }

    onClick(e){
        this.delete(this);
    }

    delete(e){
       axios.delete('http://localhost:8080/expense?id='+e.state.id,{headers:{
             'Authorization': 'Bearer ' + localStorage.getItem(ACCESS_TOKEN)
       }}).then(function(response) {});
    }

    render(){
        return(
            <Button bsStyle="danger" bsSize="small" onClick={this.onClick}>
              <Link to={{pathname: '/home', search: '?month='+this.state.month+'&year='+this.state.year}} style={{ textDecoration: 'none' }}>
                  <span className="glyphicon glyphicon-remove"></span>
              </Link>
            </Button>
        )
    }
}

export default Delete;