import React from 'react'
import './Home.css';
import Axios from 'axios';
import {Button} from 'react-bootstrap';
import Modal from 'react-modal';
import {Link} from 'react-router-dom';

class Add extends React.Component{

  constructor(){
    super();
    this.state ={
      description: '',
      amount: '',
      month: 'Jan',
      year: '2019',
      messageFromServer: '',
      modalIsOpen: false
    }

    this.handleSelectChange = this.handleSelectChange.bind(this);
    this.onClick = this.onClick.bind(this);
    this.handleTextChange = this.handleTextChange.bind(this);
    this.insertNewExpense = this.insertNewExpense.bind(this);
    this.openModal = this.openModal.bind(this);
    this.closeModal = this.closeModal.bind(this);
  }

  openModal(){
    this.setState({
      modalIsOpen: true
    });
  }

  closeModal(){
    this.setState({
      modalIsOpen: false,
      description: '',
      amount: '',
      month: 'Jan',
      year: 2019,
      messageFromServer: ''
    })
  }

  componentDidMount() {
    //alert("componentDidMount: "+this.props.selectedMonth); App call this method first time when page refresh
    this.setState({
      month: this.props.selectedMonth
    });
    this.setState({
      year: this.props.selectedYear
    });
  }

  handleSelectChange(e) {
    console.log("Month: "+e.target.value);
    if (e.target.name == 'month') {
      this.setState({
        month: e.target.value
      });
    }
    if (e.target.name == 'year') {
      this.setState({
        year: e.target.value
      });
    }
  }

  onClick(e){
    this.insertNewExpense(this);
  }

  insertNewExpense(e){
    var expense = {
      description: e.state.description,
      amount: e.state.amount,
      month: e.state.month,
      year: e.state.year
    }
    console.log("Expense: "+expense);
    Axios.post('http://localhost:8080/expense',expense).then(function(response) {
      e.setState({
        messageFromServer: response.data
      });
    });
  }

  handleTextChange(e){
    if (e.target.name == "description") {
      this.setState({
        description: e.target.value
      });
    }
    if (e.target.name == "amount") {
      this.setState({
        amount: e.target.value
      });
    }
  }

  render(){
    if(this.state.messageFromServer == ''){
      return(
        <div>
          <Button bsStyle="success" bsSize="small" onClick={this.openModal}><span className="glyphicon glyphicon-plus"></span></Button>
          <Modal isOpen={this.state.modalIsOpen} onRequestClose={this.closeModal} contentLabel="Add Expense" className="Modal">
             <Link to={{pathname: '/', search: '' }} style={{ textDecoration: 'none' }}>
              <Button bsStyle="danger" bsSize="mini" onClick={this.closeModal}>
                <span className="closebtn glyphicon glyphicon-remove"></span>
              </Button>
             </Link><br/>
             <fieldset>
                <label for="description">Description:</label>
                <input type="text" id="description" name="description" value={this.state.description} onChange={this.handleTextChange}></input>
                <label for="amount">Amount:</label>
                <input type="number" id="amount" name="amount" value={this.state.amount} onChange={this.handleTextChange}></input>
                <label for="month">Month:</label>
                <select id="month" name="month" value={this.state.month} onChange={this.handleSelectChange}>
                  <option value="Jan" id="Jan">January</option>
                  <option value="Feb" id="Feb">Febrary</option>
                  <option value="Mar" id="Mar">March</option>
                  <option value="Apr" id="Apr">April</option>
                  <option value="May" id="May">May</option>
                  <option value="Jun" id="Jun">June</option>
                  <option value="Jul" id="Jul">July</option>
                  <option value="Aug" id="Aug">August</option>
                  <option value="Sep" id="Sep">September</option>
                  <option value="Oct" id="Oct">October</option>
                  <option value="Nov" id="Nov">November</option>
                  <option value="Dec" id="Dec">December</option>
                </select>
                <label for="year">Year:</label>
                <select id="year" name="year" value={this.state.year} onChange={this.handleSelectChange}>
                  <option value="2019" id="19">2019</option>
                  <option value="2020" id="20">2020</option>
                  <option value="2021" id="21">2021</option>
                  <option value="2022" id="22">2022</option>
                  <option value="2023" id="23">2023</option>
                </select>
              </fieldset>
              <div className='button-center'>
                <br/>
                <Button bsStyle="success" bsSize="small" onClick={this.onClick}>Add New Expense</Button>
              </div>
          </Modal>
        </div>
      )
    }else{
      return (
        <div>
          <Button bsStyle="success" bsSize="small" onClick={this.openModal}><span className="glyphicon glyphicon-plus"></span></Button>
          <Modal isOpen={this.state.modalIsOpen} onAfterOpen={this.afterOpenModal} onRequestClose={this.closeModal} contentLabel="Add Expense" className="Modal">
          <div className='button-center'>
           <h3>{this.state.messageFromServer}</h3>
           <Link to={{pathname: '/', search: '?month='+this.state.month+'&year='+this.state.year}} style={{ textDecoration: 'none' }}>
            <Button bsStyle="success" bsSize="mini" onClick={this.closeModal}>Close the Dialog</Button>
           </Link>
          </div>
          </Modal>
        </div>
      )
    }
  }
}

export default Add;