import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import API from "../../api/Api";
import {sendQuery} from "../sparqlEndpoint/querySystem/querySystemSlice";


const initialState = {
    alerts: [],
    status: {},
    graphLabels: [],
    graphData:[],
    totalErrors: 0,
    request:'',
    open: false
}


export const getAlertBoxResults = createAsyncThunk('alertBox/getAlertBoxResults', async () => {
    return API.GET("getAlertBoxResults", '', [] ).then(res => {
        const results = res.data
        return {results}
    })
})

export const forceValidateEndpoints = createAsyncThunk('alertBox/forceValidateEndpoints', async () => {
    return API.GET("forceValidateEndpoints", '', [] ).then(res => {
        const results = res.data
        return {results}
    })
})



const alertBoxSlice = createSlice({
    name: 'alertBox',
    initialState,
    reducers: {
        setOpen: (state, action) => {
            state.open = action.payload
        }
    },
    extraReducers: {
        [getAlertBoxResults.pending]: (state, action) => {
            state.request = 'loading'
        },
        [getAlertBoxResults.fulfilled]: (state, action) => {
            console.log("Alert Box Results: ")
            console.log(action.payload.results)
            state.alerts = action.payload.results.list
            state.status = action.payload.results.status
            state.totalErrors = action.payload.results.totalErrors
            state.graphLabels = action.payload.results.graphLabels
            state.graphData = action.payload.results.graphData
            state.request = 'succeeded'
        }
    }
})


export const getListAlertBoxResults = state => state.alertBox.alerts
export const getAlertBoxStatus = state => state.alertBox.status
export const getGraphLabel = state => state.alertBox.graphLabels
export const getGraphData = state => state.alertBox.graphData
export const getTotalErrors = state => state.alertBox.totalErrors
export const getRequest = state => state.alertBox.request
export const getOpen = state => state.alertBox.open

export const { setOpen } = alertBoxSlice.actions

export default alertBoxSlice.reducer