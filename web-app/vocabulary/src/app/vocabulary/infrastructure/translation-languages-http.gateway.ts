import { HttpResourceRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TranslationLanguagesGateway } from '../application/translation-languages-gateway';

@Injectable({ providedIn: 'root' })
export class TranslationLanguagesHttpGateway
  implements TranslationLanguagesGateway
{
  listLanguagesRequest(): HttpResourceRequest {
    return {
      url: '/api/translate/languages'
    };
  }
}
