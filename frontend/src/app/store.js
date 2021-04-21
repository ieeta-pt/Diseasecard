import { configureStore } from '@reduxjs/toolkit'
import searchReducer from '../features/search/searchSlice'
import diseaseReducer from '../features/disease/diseaseSlice'
import browserReducer from '../features/browser/browserSlice'

export default configureStore({
    reducer: {
        disease: diseaseReducer,
        search: searchReducer,
        browser: browserReducer
    }
})