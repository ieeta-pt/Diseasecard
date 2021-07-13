import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import API from "../../api/Api";
import {sendQuery} from "../sparqlEndpoint/querySystem/querySystemSlice";


const initialState = {
    ontology: {},
    status: ''
}


export const getOntology = createAsyncThunk('sourceMap/getOntology', async () => {
    return API.GET("getOntology", '', [] ).then(res => {
        const results = res.data
        return {results}
    })
})



const sourceMapSlice = createSlice({
    name: 'sourceMap',
    initialState,
    reducers: {},
    extraReducers: {
        [getOntology.fulfilled]: (state, action) => {
            state.ontology = action.payload.results
            state.status = "fulfilled"
        }
    }
})


export const getOntologyStructure = state => state.sourceMap.ontology
export const getStatus = state => state.sourceMap.status


export default sourceMapSlice.reducer