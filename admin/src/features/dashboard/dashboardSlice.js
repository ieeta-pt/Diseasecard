import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import API from "../../api/Api";
import {sendQuery} from "../sparqlEndpoint/querySystem/querySystemSlice";


const initialState = {
    numberOfResources: "",
    numberOfConcepts: "",
    numberOfEntities: "",
    numberOfItems: "",
    numberOfInvalidItems: "",
    graphLabels: [],
    validItems: [],
    invalidItems: [],
    invalidPercentage: 0
}


export const getSystemStats = createAsyncThunk('dashboard/getSystemStats', async () => {
    return API.GET("getSystemStats", '', [] ).then(res => {
        console.log("ola")
        const results = res.data
        return {results}
    })
})



const dashboardSlice = createSlice({
    name: 'dashboard',
    initialState,
    reducers: {},
    extraReducers: {
        [getSystemStats.fulfilled]: (state, action) => {
            console.log("ola")
            console.log(action.payload.results)
            console.log(action.payload.results.numberOfEntities)

            state.numberOfResources = action.payload.results.numberOfResources
            state.numberOfConcepts = action.payload.results.numberOfConcepts
            state.numberOfEntities = action.payload.results.numberOfEntities
            state.numberOfItems = action.payload.results.numberOfItems
            state.numberOfInvalidItems = action.payload.results.numberOfInvalidItems
            state.graphLabels = action.payload.results.graphLabels
            state.validItems = action.payload.results.validItems
            state.invalidItems = action.payload.results.invalidItems
            state.invalidPercentage = ( action.payload.results.numberOfInvalidItems / action.payload.results.numberOfItems ) * 100;
        }
    }
})


export const getNumberOfResources = state => state.dashboard.numberOfResources
export const getNumberOfConcepts = state => state.dashboard.numberOfConcepts
export const getNumberOfEntities = state => state.dashboard.numberOfEntities
export const getNumberOfItems = state => state.dashboard.numberOfItems
export const getNumberOfInvalidItems = state => state.dashboard.numberOfInvalidItems
export const getGraphLabels = state => state.dashboard.graphLabels
export const getValidItems = state => state.dashboard.validItems
export const getInvalidItems = state => state.dashboard.invalidItems
export const getInvalidPercentage = state => state.dashboard.invalidPercentage


export default dashboardSlice.reducer