import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import API from "../../../api/Api";


const initialState = {
    prefixes: []
}


export const getPrefixes = createAsyncThunk('querySystem/getPrefixes', async () => {
    return API.GET("getPrefixes", '', [] ).then(res => {
        const results = res.data
        console.log("oi")
        console.log(results)
        return {results}
    })
})


const listPrefixesSlice = createSlice({
    name: 'listPrefixes',
    initialState,
    reducers: {},
    extraReducers: {
        [getPrefixes.fulfilled]: (state, action) => {
            state.prefixes = action.payload.results
        },
    }
})

export const getListOfPrefixes = state => state.listPrefixes.prefixes

export default listPrefixesSlice.reducer