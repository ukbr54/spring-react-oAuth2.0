import React, { Component } from 'react';
import { Tabs, Tab } from 'react-bootstrap'
import MonthTabs from './tabs/monthTabs';
import YearTabsRouter from './tabs/yearTabsRouter'
import Add from './Add';
import Update from './Update';
import Delete from './Delete';
import Axios from 'axios';
import {ACCESS_TOKEN} from '../../constants'

class Home extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedMonth: 'Jan',
            selectedYear: 2019,
            data: [],
            activeTab: 2019
        }
        this.getData = this.getData.bind(this);
        this.handleSelect = this.handleSelect.bind(this);
    }
    

    componentDidMount() { 
        this.getData(this, 2019, 'All');
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.history.location.search) {
            var search = nextProps.history.location.search;
            console.log("Search: " + search);
            search = search.substring(1);
            var searchObj = JSON.parse('{"' + decodeURI(search).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g, '":"') + '"}');
            this.setState({ activeTab: parseInt(searchObj.year) });
            this.setState({ selectedYear: searchObj.year });
            this.setState({ selectedMonth: searchObj.month });
            this.getData(this, searchObj.year, searchObj.month);
        } else {
            this.getData(this, 2019, 'All');
        }
    }

    handleSelect(selectedTab) {
        console.log("Selected Tab: " + selectedTab);
        this.setState({
            activeTab: selectedTab,
            selectedYear: selectedTab
        });
    }

    getData(ev, year, month) {
        Axios.get('http://localhost:8080/api/expense/' + year + '/' + month,{headers:{
            'Authorization': 'Bearer ' + localStorage.getItem(ACCESS_TOKEN)
        }}).then(function (response) {
            ev.setState({ data: response.data });
            ev.setState({ selectedYear: parseInt(year) });
            ev.setState({ selectedMonth: month });
        });
    }

    render() {
        return (
            <div>
                <Tabs activeKey={this.state.activeTab} onSelect={this.handleSelect}>
                    <Tab eventKey={2019} title={<YearTabsRouter year='2019' />}>
                        <MonthTabs year='2019' monthlyActiveTab={this.state.selectedMonth} />
                    </Tab>
                    <Tab eventKey={2020} title={<YearTabsRouter year='2020' />}>
                        <MonthTabs year='2020' monthlyActiveTab={this.state.selectedMonth} />
                    </Tab>
                    <Tab eventKey={2021} title={<YearTabsRouter year='2021' />}>
                        <MonthTabs year='2021' monthlyActiveTab={this.state.selectedMonth} />
                    </Tab>
                    <Tab eventKey={2022} title={<YearTabsRouter year='2022' />}>
                        <MonthTabs year='2022' monthlyActiveTab={this.state.selectedMonth} />
                    </Tab>
                    <Tab eventKey={2023} title={<YearTabsRouter year='2023' />}>
                        <MonthTabs year='2023' monthlyActiveTab={this.state.selectedMonth} />
                    </Tab>
                </Tabs>
                <Add selectedMonth={this.state.selectedMonth} selectedYear={this.state.selectedYear} />
                <table>
                    <thead>
                        <tr>
                            <th></th>
                            <th className='desc-col'>Description</th>
                            <th className='button-col'>Amount</th>
                            <th className='button-col'>Month</th>
                            <th className='button-col'>Year</th>
                            <th className='button-col'>Update</th>
                            <th className='button-col'>Delete</th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                            this.state.data.map(function (exp) {
                                return <tr>
                                    <td className='counterCell'></td>
                                    <td className='desc-col'>{exp.description}</td>
                                    <td className='button-col'>{exp.amount}</td>
                                    <td className='button-col'>{exp.month}</td>
                                    <td className='button-col'>{exp.year}</td>
                                    <td className='button-col'><Update expense={exp} /></td>
                                    <td className='button-col'><Delete id={exp._id} expense={exp} /></td>
                                </tr>
                            })
                        }
                    </tbody>
                </table>
            </div>
        )
    }
}

export default Home;