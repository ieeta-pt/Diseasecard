import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import API from "../../../api/Api";


const initialState = {
    results: {},
    prefixes: {},
    status: ""
}

export const sendQuery = createAsyncThunk('querySystem/sendQuery', async (form) => {
    return API.POST("sendQuery", '', form ).then(res => {
        const results = res.data
        return {results}
    })
})


const querySystemSlice = createSlice({
    name: 'querySystem',
    initialState,
    reducers: {
        updateStatus:(state, action) => {
            state.status = action.payload
        }
    },
    extraReducers: {
        [sendQuery.pending]: (state, action) => {
            state.status = 'loading'
        },
        [sendQuery.fulfilled]: (state, action) => {
            state.status = 'succeeded'
            state.results = action.payload.results
        },
        [sendQuery.rejected]: (state, action) => {
            state.status = 'failed'
            //state.error = action.error.message
        },
    }
})

export const getQueryResult  = state => state.querySystem.results
export const getStatus = state => state.querySystem.status

export const { updateStatus } = querySystemSlice.actions

export default querySystemSlice.reducer